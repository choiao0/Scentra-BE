package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.CategoryMapping;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.ProductLikes;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.CategoryType;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.CategoryMappingRepository;
import com.apollo.scentraapi.repository.CategoryRepository;
import com.apollo.scentraapi.repository.ProductLikesRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private CategoryMappingRepository categoryMappingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductLikesRepository productLikesRepository;

    @MockBean
    private S3Service s3Service;

    private User testUser;
    private Product productA1;
    private Product productA2;
    private Product productB1;
    private Category floral;
    private Category warm;

    @BeforeEach
    void setUp() {
        testUser = createUser("user", "user@example.com", Gender.MALE, "010-0000-0000");
        userRepository.save(testUser);

        Brand brandA = createBrand("브랜드A", "brandA");
        productA1 = createProduct(brandA, "상품A1", "productA1", 150000d);
        productA2 = createProduct(brandA, "상품A2", "productA2", 270000d);

        Brand brandB = createBrand("브랜드B", "brandB");
        productB1 = createProduct(brandB, "상품B1", "productB1", 160000d);

        brandRepository.saveAll(List.of(brandA, brandB));
        productRepository.saveAll(List.of(productA1, productA2, productB1));

        floral = createCategory("플로럴", "floral", CategoryType.NOTE);
        warm = createCategory("웜", "warm", CategoryType.MOOD);
        categoryRepository.saveAll(List.of(floral, warm));
    }

    @DisplayName("상품 ID로 상품 정보를 조회한다.")
    @Test
    void should_ReturnProductInfo_When_ProductExists() {
        // given
        Long productId = productA1.getId();

        // when
        ProductResponse.ProductDto response = productService.getProduct(productId);

        // then
        assertThat(response.getBrandNameEn()).isEqualTo("brandA");
        assertThat(response.getProductNameEn()).isEqualTo("productA1");
        assertThat(response.getPrice()).isEqualTo(150000d);
    }

    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ProductDoesNotExist() {
        // given
        Long invalidProductId = 999L;

        // when, then
        assertThatThrownBy(() -> productService.getProduct(invalidProductId))
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("모든 상품 목록을 조회한다.")
    @Test
    void should_ReturnAllProducts_When_ProductsExist() {
        // given

        // when
        List<ProductResponse.ProductListDto> response = productService.getAllProducts();

        // then
        assertThat(response).hasSize(3);
        assertThat(response)
                .extracting("brandNameEn", "productNameEn", "price")
                .containsExactlyInAnyOrder(
                        tuple("brandA", "productA1", 150000d),
                        tuple("brandA", "productA2", 270000d),
                        tuple("brandB", "productB1", 160000d)
                );
    }

    @DisplayName("상품을 등록하면 정상적으로 업로드되고 카테고리 매핑이 생성된다.")
    @Test
    void should_SaveProductAndCategoryMappings_When_ValidRequest() {
        // given
        given(s3Service.uploadFile(any(MultipartFile.class)))
                .willReturn("http://dummy-s3-url/logo.png");

        ProductRequest.ProductUploadDto request = new ProductRequest.ProductUploadDto();
        ReflectionTestUtils.setField(request, "productNameKr", "상품B2");
        ReflectionTestUtils.setField(request, "productNameEn", "productB2");
        ReflectionTestUtils.setField(request, "brandNameEn", "brandB");
        ReflectionTestUtils.setField(request, "price", 280000d);
        ReflectionTestUtils.setField(request, "targetGender", "MALE");
        List<String> categories = List.of("플로럴", "웜");
        ReflectionTestUtils.setField(request, "category", categories);

        MockMultipartFile productImage = new MockMultipartFile(
                "productImage", "product.png", "image/png", "dummy-image-bytes".getBytes()
        );
        MockMultipartFile detailImage = new MockMultipartFile(
                "detailImage", "detail.png", "image/png", "dummy-image-bytes".getBytes()
        );

        // when
        productService.uploadProduct(productImage, detailImage, request);

        // then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(4);
        List<CategoryMapping> categoryMappings = categoryMappingRepository.findAll();
        assertThat(categoryMappings).hasSize(2);
    }

    @DisplayName("상품 정보 변경 DTO에서 null이 아닌 필드만 상품 정보에 반영된다.")
    @Test
    void should_UpdateProduct_When_ProductExists() {
        // given
        Long productId = productB1.getId();
        ProductRequest.ProductUpdateRequestDTO request = new ProductRequest.ProductUpdateRequestDTO();
        ReflectionTestUtils.setField(request, "productNameEn", "product(B1)");
        ReflectionTestUtils.setField(request, "targetGender", "FEMALE");

        // when
        productService.updateProduct(productId, request);

        // then
        Product updatedProduct = productRepository.findById(productId)
                        .orElseThrow(() -> new AssertionError("상품이 존재해야 합니다."));
        assertThat(updatedProduct.getProductNameKr()).isEqualTo("상품B1");
        assertThat(updatedProduct.getProductNameEn()).isEqualTo("product(B1)");
        assertThat(updatedProduct.getPrice()).isEqualTo(160000d);
        assertThat(updatedProduct.getTargetGender()).isEqualTo(Gender.FEMALE);
    }

    @DisplayName("상품 ID로 상품을 삭제한다.")
    @Test
    void should_DeleteProduct_When_ProductExists() {
        // given
        Long productId = productA2.getId();
        doNothing().when(s3Service).deleteImage(anyString());

        // when
        productService.deleteProduct(productId);

        // then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("productNameEn")
                .containsExactlyInAnyOrder("productA1", "productB1");
    }

    @DisplayName("좋아요하지 않은 상품에 좋아요를 추가한다.")
    @Test
    void should_SaveProductLike_When_NotLikedBefore() {
        // given
        Long productId = productA1.getId();

        // when
        productService.addLike(testUser, productId);

        // then
        List<ProductLikes> productLikes = productLikesRepository.findAll();
        assertThat(productLikes).hasSize(1);

        ProductLikes like = productLikes.get(0);
        assertThat(like.getUser()).isEqualTo(testUser);
        assertThat(like.getProduct()).isEqualTo(productA1);
    }

    @DisplayName("좋아요한 상품의 좋아요를 삭제한다.")
    @Test
    void should_DeleteProductLike_When_LikeExists() {
        // given
        Long productId = productA1.getId();
        ProductLikes productLikes = createProductLikes(testUser, productA1);
        productLikesRepository.save(productLikes);

        // when
        productService.removeLike(testUser, productId);

        // then
        List<ProductLikes> findProductLikes = productLikesRepository.findAll();
        assertThat(findProductLikes).isEmpty();
    }

    @DisplayName("키워드로 상품/브랜드명을 매칭하여 반환한다.")
    @Test
    void should_ReturnMatchingProductsAndBrands_When_KeywordMatches() {
        // given
        String keywordA = "brandA";
        String keywordB = "productB";

        // when
        List<ProductResponse.ProductListDto> responseA = productService.searchProducts(keywordA);
        List<ProductResponse.ProductListDto> responseB = productService.searchProducts(keywordB);

        // then
        assertThat(responseA).hasSize(2);
        assertThat(responseA)
                .extracting("brandNameEn", "productNameEn", "price")
                .containsExactlyInAnyOrder(
                        tuple("brandA", "productA1", 150000d),
                        tuple("brandA", "productA2", 270000d)
                );

        assertThat(responseB).hasSize(1);
        assertThat(responseB)
                .extracting("brandNameEn", "productNameEn", "price")
                .containsExactlyInAnyOrder(
                        tuple("brandB", "productB1", 160000d)
                );
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

    private Category createCategory(String nameKr, String nameEn, CategoryType type) {
        return Category.builder()
                .categoryNameKr(nameKr)
                .categoryNameEn(nameEn)
                .categoryType(type)
                .build();
    }

    private ProductLikes createProductLikes(User user, Product product) {
        return ProductLikes.builder()
                .user(user)
                .product(product)
                .build();
    }
}
