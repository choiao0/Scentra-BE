package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.converter.CategoryConverter;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.repository.*;
import com.apollo.scentraapi.repository.BrandRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryMappingRepository categoryMappingRepository;
    private final CategoryRepository categoryRepository;
    private final ProductLikesRepository productLikeRepository;

    @Transactional
    public ProductResponse.ProductDto getProduct(Long id) {
        // 1. 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));

        return ProductConverter.toProductResponse(product);
    }

    public List<ProductResponse.ProductListDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse.ProductListDto> productList = new ArrayList<>();

        if (products.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        for (Product product : products) {
            Long brand_id = product.getBrand().getId();
            Brand brand = brandRepository.findById(brand_id)
                    .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
            String brandNameKr = brand.getBrandNameKr();
            String brandNameEn = brand.getBrandNameEn();
            ProductResponse.ProductListDto product_dto = ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
            productList.add(product_dto);
        }
        return productList;
    }

    @Transactional
    public Product uploadProduct(MultipartFile productImage, MultipartFile detailImage, ProductRequest.ProductUploadDto productUploadDto) {
        String productImageUrl = s3Service.uploadFile(productImage);
        String detailImageUrl = null;
        if (detailImage != null) detailImageUrl = s3Service.uploadFile(detailImage);
        Product new_product = ProductConverter.toProduct(productImageUrl, detailImageUrl, productUploadDto);
        Brand brand = brandRepository.findByBrandNameEn(productUploadDto.getBrandNameEn())
                .orElseGet(() -> brandRepository.findByBrandNameKr(productUploadDto.getBrandNameKr())
                .orElseThrow(() -> new ProductException(ErrorStatus.BRAND_NOT_FOUND)));
        new_product.setBrand(brand);
        new_product = productRepository.save(new_product);

        for (String c : productUploadDto.getCategory()) {
            Category category = categoryRepository.findByCategoryNameKr(c)
                    .orElseThrow(() -> new ProductException(ErrorStatus.CATEGORY_NOT_FOUND));
            CategoryMapping mapping = CategoryConverter.toCategoryMapping(category, new_product);
            categoryMappingRepository.save(mapping);
        }

        return new_product;
    }

    @Transactional
    public ProductResponse.ProductUpdateResponseDTO updateProduct(Long id, ProductRequest.ProductUpdateRequestDTO request) {
        // 1. 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));

        // 2. 브랜드 변경이 있을 경우, 브랜드 찾기
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
            product.setBrand(brand);  // ✅ 브랜드 정보 업데이트
        }

        // 3. 상품 정보 업데이트
        product.update(
                request.getProductNameKr(),
                request.getProductNameEn(),
                request.getProductImage(),
                request.getDetailImage(),
                request.getPrice(),
                request.getTargetGender()
        );

        // 4. 응답 DTO 반환
        return ProductConverter.toProductUpdateResponseDTO(product);
    }

    @Transactional
    public ProductResponse.ProductDeleteResponseDTO deleteProduct(Long id) {
        // 1. 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));

        // 2. 삭제 수행
        s3Service.deleteImage(product.getProductImage());
        if (product.getDetailImage() != null) s3Service.deleteImage(product.getDetailImage());
        productRepository.delete(product);

        // 3. 삭제된 상품 정보 반환
        return ProductConverter.toProductDeleteResponseDTO(product);
    }

    public ProductResponse.ImageDTO createBackgroundImage(ProductRequest.CreateBgImgDTO request) {

        String prompt = request.getPrompt();

        /*
            AI 서버로 프롬프트 넘겨주고, 생성된 배경 이미지 수신
         */

        String imageUrl = "http://"+prompt;

        return ProductConverter.toImageDTO(imageUrl);
    }

    public ProductResponse.ImageDTO createCompositeImage(ProductRequest.CreateCompositeImgDTO request) {

        String backgroundImageUrl = request.getBackgroundImageUrl();
        String productImageUrl = request.getProductImageUrl();

        /*
            배경 및 상품 이미지 넘겨주고, 생성된 합성 이미지 수신
         */

        String imageUrl = "http://"+backgroundImageUrl+"/"+productImageUrl;

        return ProductConverter.toImageDTO(imageUrl);
    }

    public ProductResponse.ProductLikeDTO addLike(User user, Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));

        Optional<ProductLikes> findProductLikes = productLikeRepository.findByUserAndProduct(user, product);
        if (findProductLikes.isPresent())
            throw new ProductException(ErrorStatus.PRODUCT_ALREADY_LIKED);

        ProductLikes newLike = ProductConverter.toProductLike(product, user);
        productLikeRepository.save(newLike);

        return ProductConverter.toProductLikeDTO(newLike);
    }

    public ProductResponse.ProductLikeDTO removeLike(User user, Long productId) {
        Optional<ProductLikes> optionalProductLike = productLikeRepository.findByUserIdAndProductId(user.getId(), productId);
        ProductLikes productLike = optionalProductLike.orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_LIKED));

        productLikeRepository.delete(productLike);
        return ProductConverter.toProductLikeDTO(productLike);
    }
    @Transactional(readOnly = true)
    public List<ProductResponse.ProductListDto> getProductsByCategory(Long category_id) {
        List<CategoryMapping> mappings = categoryMappingRepository.findByCategoryId(category_id);

        if (mappings.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND); // 예외 처리 추가
        }
        return mappings.stream()
                .map(mapping -> {
                    Product product = mapping.getProduct();
                    String brandNameKr = product.getBrand().getBrandNameKr();
                    String brandNameEn = product.getBrand().getBrandNameEn();
                    return ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
                })
                .collect(Collectors.toList());
    }

    public List<ProductResponse.ProductListDto> searchProducts(String keyword) {
        // 1. 검색어가 null 또는 빈 문자열일 경우 예외 처리
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ProductException(ErrorStatus.INVALID_SEARCH_KEYWORD);
        }

        // 2. 검색 실행
        List<Product> filteredProducts = productRepository.findAll().stream()
                .filter(product ->
                        (product.getProductNameKr() != null && product.getProductNameKr().contains(keyword)) ||  // (1) 키워드가 상품명에 포함됨
                        (product.getProductNameEn() != null && product.getProductNameEn().toLowerCase().contains(keyword.toLowerCase())) ||
                        (product.getBrand().getBrandNameKr() != null && product.getBrand().getBrandNameKr().contains(keyword)) ||  // (2) 키워드가 브랜드명에 포함됨
                        (product.getBrand().getBrandNameEn() != null && product.getBrand().getBrandNameEn().toLowerCase().contains(keyword.toLowerCase())
                        ))
                .toList();


        // 3. 검색 결과 없을 경우 예외 처리
        if (filteredProducts.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        return filteredProducts.stream()
                .map(product -> {
                    String brandNameKr = product.getBrand().getBrandNameKr();
                    String brandNameEn = product.getBrand().getBrandNameEn();
                    return ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
                })
                .collect(Collectors.toList());

    }

    public ProductResponse.ProductLikeDTO isLike(User user, Long productId) {
        Optional<ProductLikes> optionalProductLike = productLikeRepository.findByUserIdAndProductId(user.getId(), productId);
        ProductLikes productLike = optionalProductLike.orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_LIKED));
        return ProductConverter.toProductLikeDTO(productLike);
    }
}
