package com.apollo.scentraapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    private static Brand brandA;
    private static Brand brandB;

    @BeforeAll
    void setUp() {
        brandA = createBrand("딥디크", "Diptique");
        Product product2 = createProduct(brandA, "도손", "Doson", 150000d);
        Product product3 = createProduct(brandA, "탐다오", "Tamdao", 220000d);

        brandB = createBrand("디올", "Dior");
        Product product1 = createProduct(brandB, "미스 디올", "Miss Dior", 130000d);

        brandRepository.saveAll(List.of(brandA, brandB));
        productRepository.saveAll(List.of(product1, product2, product3));
    }

    @DisplayName("브랜드 관계 없이 모든 상품을 조회한다.")
    @Test
    void findAll() {
        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(3);
        assertThat(products).extracting("productNameKr")
                .containsExactlyInAnyOrder("도손", "탐다오", "미스 디올");
    }

    @DisplayName("브랜드 별 상품 개수를 조회한다.")
    @Test
    void countByBrand(){
        // when
        Long countByBrandA = productRepository.countByBrand(brandA);
        Long countByBrandB = productRepository.countByBrand(brandB);

        // then
        assertThat(countByBrandA).isEqualTo(2L);
        assertThat(countByBrandB).isEqualTo(1L);
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
