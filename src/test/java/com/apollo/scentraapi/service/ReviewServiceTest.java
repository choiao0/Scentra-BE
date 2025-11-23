package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.apollo.scentraapi.apiPayload.exception.handler.CartException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Cart;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.CartRequest;
import com.apollo.scentraapi.dto.response.CartResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.CartRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.ReviewRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private Product productA2;
    private Product productB1;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();

        testUser = createUser("user", "user@example.com", Gender.MALE, "010-0000-0000");
        userRepository.save(testUser);

        Brand brandA = createBrand("브랜드A", "brandA");
        productA1 = createProduct(brandA, "상품A1", "productA1", 150000d);
        productA2 = createProduct(brandA, "상품A2", "productA2", 270000d);

        Brand brandB = createBrand("브랜드B", "brandB");
        productB1 = createProduct(brandB, "상품B1", "productB1", 160000d);

        brandRepository.saveAll(List.of(brandA, brandB));
        productRepository.saveAll(List.of(productA1, productA2, productB1));
    }

    @DisplayName("유저가 상품에 리뷰를 정상적으로 작성한다.")
    @Test
    void should_CreateReview_When_UserAndProductAreValid() {

    }

    @DisplayName("리뷰 생성 시 유저나 상품 정보가 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserOrProductNotFound() {

    }

    @DisplayName("자신이 작성한 리뷰를 수정한다.")
    @Test
    void should_UpdateReview_When_UserIsAuthor() {

    }

    @DisplayName("자신이 작성한 리뷰를 삭제한다.")
    @Test
    void should_DeleteReview_When_UserIsAuthor() {

    }

    @DisplayName("자신이 작성하지 않은 리뷰를 수정/삭제하려고 하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserIsNotReviewAuthor() {

    }

    @DisplayName("수정/삭제하려는 리뷰의 ID가 잘못된 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ReviewIdIsInvalid() {

    }

    @DisplayName("상품에 작성된 모든 리뷰를 조회한다.")
    @Test
    void should_GetAllReviews_When_ProductIdIsValid() {

    }

    @DisplayName("리뷰 조회 시 상품 정보가 잘못된 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ProductIdIsInvalid() {

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