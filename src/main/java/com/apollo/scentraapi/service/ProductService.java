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
        Product product = getProductOrThrow(id);
        return ProductConverter.toProductResponse(product);
    }
    public List<ProductResponse.ProductListDto> getAllProducts() {
        List<Product> products = productRepository.findAllWithBrand();
        List<ProductResponse.ProductListDto> productList = new ArrayList<>();

        if (products.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        for (Product product : products) {
            Brand brand = product.getBrand();
            String brandNameKr = brand.getBrandNameKr();
            String brandNameEn = brand.getBrandNameEn();
            ProductResponse.ProductListDto productDto = ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
            productList.add(productDto);
        }
        return productList;
    }

    @Transactional
    public ProductResponse.ProductDto uploadProduct(MultipartFile productImage, MultipartFile detailImage, ProductRequest.ProductUploadDto productUploadDto) {
        String productImageUrl = s3Service.uploadFile(productImage);

        String detailImageUrl = null;
        if (detailImage != null) {
            detailImageUrl = s3Service.uploadFile(detailImage);
        }

        Product newProduct = ProductConverter.toProduct(productImageUrl, detailImageUrl, productUploadDto);
        Brand brand = brandRepository.findByBrandNameEn(productUploadDto.getBrandNameEn())
                .or(() -> brandRepository.findByBrandNameKr(productUploadDto.getBrandNameKr()))
                .orElseThrow(() -> new ProductException(ErrorStatus.BRAND_NOT_FOUND));
        newProduct.setBrand(brand);
        newProduct = productRepository.save(newProduct);

        for (String c : productUploadDto.getCategory()) {
            Category category = categoryRepository.findByCategoryNameKr(c)
                    .orElseThrow(() -> new ProductException(ErrorStatus.CATEGORY_NOT_FOUND));
            CategoryMapping mapping = CategoryConverter.toCategoryMapping(category, newProduct);
            categoryMappingRepository.save(mapping);
        }

        return ProductConverter.toProductResponse(newProduct);
    }

    @Transactional
    public ProductResponse.ProductUpdateResponseDTO updateProduct(Long id, ProductRequest.ProductUpdateRequestDTO request) {
        Product product = getProductOrThrow(id);
        Long updateBrandId = request.getBrandId();

        if (updateBrandId != null) {
            Brand brand = getBrandOrThrow(updateBrandId);
            product.setBrand(brand);
        }

        product.update(
                request.getProductNameKr(),
                request.getProductNameEn(),
                request.getProductImage(),
                request.getDetailImage(),
                request.getPrice(),
                request.getTargetGender()
        );

        return ProductConverter.toProductUpdateResponseDTO(product);
    }

    @Transactional
    public ProductResponse.ProductDeleteResponseDTO deleteProduct(Long id) {
        Product product = getProductOrThrow(id);

        s3Service.deleteImage(product.getProductImage());
        if (product.getDetailImage() != null) {
            s3Service.deleteImage(product.getDetailImage());
        }
        productRepository.delete(product);

        return ProductConverter.toProductDeleteResponseDTO(product);
    }

    public ProductResponse.ProductLikeDTO addLike(User user, Long productId) {
        Product product = getProductOrThrow(productId);

        if (existProductLike(user, product)) {
            throw new ProductException(ErrorStatus.PRODUCT_ALREADY_LIKED);
        }

        ProductLikes newLike = ProductConverter.toProductLike(product, user);
        productLikeRepository.save(newLike);

        return ProductConverter.toProductLikeDTO(newLike);
    }

    public ProductResponse.ProductLikeDTO removeLike(User user, Long productId) {
        Product product = getProductOrThrow(productId);
        ProductLikes productLike = getProductLikeOrThrow(user, product);

        productLikeRepository.delete(productLike);
        return ProductConverter.toProductLikeDTO(productLike);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.ProductListDto> getProductsByCategory(Long categoryId) {
        List<CategoryMapping> mappings = categoryMappingRepository.findByCategoryId(categoryId);

        if (mappings.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        return mappings.stream()
                .map(mapping -> {
                    Product product = mapping.getProduct();
                    String brandNameKr = product.getBrand().getBrandNameKr();
                    String brandNameEn = product.getBrand().getBrandNameEn();
                    return ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
                }).toList();
    }

    public List<ProductResponse.ProductListDto> searchProducts(String keyword) {
        // 검색어가 null 또는 빈 문자열일 경우 예외 처리
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ProductException(ErrorStatus.INVALID_SEARCH_KEYWORD);
        }

        // 검색 실행
        List<Product> filteredProducts = productRepository.findAll().stream()
                .filter(product -> product.matchesKeyword(keyword))
                .toList();


        // 검색 결과 없을 경우 예외 처리
        if (filteredProducts.isEmpty()) {
            throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        return filteredProducts.stream()
                .map(product -> {
                    String brandNameKr = product.getBrand().getBrandNameKr();
                    String brandNameEn = product.getBrand().getBrandNameEn();
                    return ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
                }).toList();
    }

    public ProductResponse.ProductLikeDTO isLike(User user, Long productId) {
        Product product = getProductOrThrow(productId);
        ProductLikes productLike = getProductLikeOrThrow(user, product);
        return ProductConverter.toProductLikeDTO(productLike);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));
    }

    private Brand getBrandOrThrow(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
    }

    private ProductLikes getProductLikeOrThrow(User user, Product product) {
        return productLikeRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_LIKED));
    }

    private boolean existProductLike(User user, Product product) {
        return productLikeRepository.findByUserAndProduct(user, product).isPresent();
    }
}
