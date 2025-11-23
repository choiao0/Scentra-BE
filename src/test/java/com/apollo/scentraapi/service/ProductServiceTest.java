package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.BrandLikes;
import com.apollo.scentraapi.domain.Seller;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.CategoryMappingRepository;
import com.apollo.scentraapi.repository.CategoryRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    CategoryMappingRepository categoryMappingRepository;

    @DisplayName("상품 ID로 상품 정보를 조회한다.")
    @Test
    void should_ReturnProductInfo_When_ProductExists() {

    }

    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ProductDoesNotExist() {

    }

    @DisplayName("모든 상품 목록을 조회한다.")
    @Test
    void should_ReturnAllProducts_When_ProductsExist() {

    }

    @DisplayName("상품을 등록하면 정상적으로 업로드되고 카테고리 매핑이 생성된다.")
    @Test
    void should_SaveProductAndCategoryMappings_When_ValidRequest() {

    }

    @DisplayName("상품 정보를 정상적으로 수정한다.")
    @Test
    void should_UpdateProduct_When_ProductExists() {

    }

    @DisplayName("상품과 S3에 업로드된 이미지를 삭제한다.")
    @Test
    void should_DeleteProduct_When_ProductExists() {

    }

    @DisplayName("좋아요하지 않은 상품에 좋아요를 추가한다.")
    @Test
    void should_SaveProductLike_When_NotLikedBefore() {

    }

    @DisplayName("좋아요한 상품의 좋아요를 삭제한다.")
    @Test
    void should_DeleteProductLike_When_LikeExists() {

    }

    @DisplayName("키워드로 상품/브랜드명을 매칭하여 반환한다.")
    @Test
    void should_ReturnMatchingProductsAndBrands_When_KeywordMatches() {

    }
}
