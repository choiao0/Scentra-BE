package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.ReviewRequest;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.ReviewRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;

    private User testUser;
    private Product productA1;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        productRepository.deleteAll();
        brandRepository.deleteAll();
        userRepository.deleteAll();

        testUser = createUser("user", "user@example.com", Gender.MALE, "010-0000-0000");
        userRepository.save(testUser);

        Brand brandA = createBrand("브랜드A", "brandA");
        productA1 = createProduct(brandA, "상품A1", "productA1", 150000d);

        brandRepository.save(brandA);
        productRepository.save(productA1);
    }

    @DisplayName("유저가 상품에 리뷰를 정상적으로 작성한다.")
    @Test
    void should_CreateReview_When_ProductIsValid() {
        // given
        ReviewRequest.ReviewCreateDTO request = new ReviewRequest.ReviewCreateDTO();
        ReflectionTestUtils.setField(request, "content", "Good!");
        ReflectionTestUtils.setField(request, "rating", 5);

        // when
        Long productId = productA1.getId();
        reviewService.createReview(testUser, productId, request);

        // then
        List<Review> reviews = reviewRepository.findAllByProduct(productA1);
        assertThat(reviews).hasSize(1);

        Review review = reviews.get(0);
        assertThat(review.getContent()).isEqualTo("Good!");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @DisplayName("리뷰 생성 시 상품 정보가 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ProductNotFound() {
        // given
        Long invalidProductId = 999L;

        ReviewRequest.ReviewCreateDTO request = new ReviewRequest.ReviewCreateDTO();
        ReflectionTestUtils.setField(request, "content", "Good!");
        ReflectionTestUtils.setField(request, "rating", 5);

        // when, then
        assertThatThrownBy(() -> reviewService.createReview(testUser, invalidProductId, request))
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("자신이 작성한 리뷰를 수정한다.")
    @Test
    void should_UpdateReview_When_UserIsAuthor() {
        // given
        Review review = createReview(testUser, productA1, "Good!", 5);
        reviewRepository.save(review);

        ReviewRequest.ReviewUpdateDTO request = new ReviewRequest.ReviewUpdateDTO();
        ReflectionTestUtils.setField(request, "content", "Bad");
        ReflectionTestUtils.setField(request, "rating", 1);

        // when
        Long reviewId = review.getId();
        reviewService.updateReview(testUser, reviewId, request);

        // then
        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AssertionError("리뷰가 존재해야 합니다."));
        assertThat(findReview.getContent()).isEqualTo("Bad");
        assertThat(findReview.getRating()).isEqualTo(1);
    }

    @DisplayName("자신이 작성한 리뷰를 삭제한다.")
    @Test
    void should_DeleteReview_When_UserIsAuthor() {
        // given
        Review review = createReview(testUser, productA1, "Good!", 5);
        reviewRepository.save(review);

        // when
        Long reviewId = review.getId();
        reviewService.deleteReview(testUser, reviewId);

        // then
        List<Review> reviews = reviewRepository.findAllByProduct(productA1);
        assertThat(reviews).isEmpty();
    }

    @DisplayName("자신이 작성하지 않은 리뷰를 수정/삭제하려고 하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserIsNotReviewAuthor() {
        // given
        User newUser = createUser("newUser", "newUser@example.com", Gender.FEMALE, "010-1111-1111");
        userRepository.save(newUser);
        Review review = createReview(newUser, productA1, "Good!", 5);
        reviewRepository.save(review);

        ReviewRequest.ReviewUpdateDTO request = new ReviewRequest.ReviewUpdateDTO();
        ReflectionTestUtils.setField(request, "content", "Bad");
        ReflectionTestUtils.setField(request, "rating", 1);

        // when, then
        Long reviewId = review.getId();
        assertThatThrownBy(() -> reviewService.updateReview(testUser, reviewId, request))
                .isInstanceOf(ProductException.class);
        assertThatThrownBy(() -> reviewService.deleteReview(testUser, reviewId))
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("수정/삭제하려는 리뷰의 ID가 잘못된 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ReviewIdIsInvalid() {
        // given
        Long invalidReviewId = 999L;

        ReviewRequest.ReviewUpdateDTO request = new ReviewRequest.ReviewUpdateDTO();
        ReflectionTestUtils.setField(request, "content", "Bad");
        ReflectionTestUtils.setField(request, "rating", 1);

        // when, then
        assertThatThrownBy(() -> reviewService.updateReview(testUser, invalidReviewId, request))
                .isInstanceOf(ProductException.class);
        assertThatThrownBy(() -> reviewService.deleteReview(testUser, invalidReviewId))
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("상품에 작성된 모든 리뷰를 조회한다.")
    @Test
    void should_GetAllReviews_When_ProductIdIsValid() {
        // given
        Review review1 = createReview(testUser, productA1, "Very Good!", 5);
        Review review2 = createReview(testUser, productA1, "Good", 3);
        Review review3 = createReview(testUser, productA1, "Bad", 1);
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        Long productId = productA1.getId();
        List<Review> response = reviewService.getReviewList(productId);

        // then
        assertThat(response).hasSize(3);
        assertThat(response)
                .extracting("content", "rating")
                .containsExactlyInAnyOrder(
                        tuple("Very Good!", 5),
                        tuple("Good", 3),
                        tuple("Bad", 1)
                );
    }

    @DisplayName("상품의 모든 리뷰 조회 시 상품 정보가 잘못된 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ProductIdIsInvalid() {
        // given
        Review review1 = createReview(testUser, productA1, "Very Good!", 5);
        Review review2 = createReview(testUser, productA1, "Good", 3);
        Review review3 = createReview(testUser, productA1, "Bad", 1);
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        Long invalidProductId = 999L;
        assertThatThrownBy(() -> reviewService.getReviewList(invalidProductId))
                .isInstanceOf(ProductException.class);
    }

    private Review createReview(User user, Product product, String content, Integer rating) {
        return Review.builder()
                .user(user)
                .product(product)
                .content(content)
                .rating(rating)
                .build();
    }

    private User createUser(String name, String email, Gender gender, String phoneNum){
        return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .phoneNum(phoneNum)
                .build();
    }

    private Brand createBrand(String nameKr, String nameEn) {
        return Brand.builder()
                .brandNameKr(nameKr)
                .brandNameEn(nameEn)
                .build();
    }

    private Product createProduct(Brand brand, String nameKr, String nameEn, Double price) {
        return Product.builder()
                .brand(brand)
                .productNameKr(nameKr)
                .productNameEn(nameEn)
                .price(price)
                .build();
    }
}