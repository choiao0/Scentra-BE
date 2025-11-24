package com.apollo.scentraapi.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품에 연결된 리뷰 목록을 조회한다.")
    @Test
    void findAllByProduct(){
        // given
        Product product = createProduct("미스 디올", "Miss Dior", 130000d);
        productRepository.save(product);

        Review review1 = createReview(product, "좋아요!", 5);
        Review review2 = createReview(product, "괜찮아요.", 3);
        Review review3 = createReview(product, "별로예요.", 1);
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        List<Review> reviews = reviewRepository.findAllByProduct(product);

        // then
        assertThat(reviews).hasSize(3);
        assertThat(reviews).extracting("content", "rating")
                .containsExactlyInAnyOrder(
                        tuple("좋아요!", 5),
                        tuple("괜찮아요.", 3),
                        tuple("별로예요.", 1)
                );
    }

    private Product createProduct(String nameKr, String nameEn, Double price){
        return Product.builder()
                .productNameKr(nameKr)
                .productNameEn(nameEn)
                .price(price)
                .build();
    }

    private Review createReview(Product product, String content, int rating) {
        return Review.builder()
                .product(product)
                .content(content)
                .rating(rating)
                .build();
    }
}
