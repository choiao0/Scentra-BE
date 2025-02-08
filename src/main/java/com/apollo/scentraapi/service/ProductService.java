package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.apollo.scentraapi.apiPayload.exception.ProductNotFoundException;
import com.apollo.scentraapi.apiPayload.exception.BrandNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final BrandRepository brandRepository;

    @Transactional
    public ProductResponse.ProductGetResponseDTO getProduct(Long id) {
        // 1. 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return ProductResponse.ProductGetResponseDTO.builder()
                .name(product.getProductName())
                .productImage(product.getProductImage())
                .detailImage(product.getDetailImage())
                .description(product.getProductDescription())
                .price(product.getPrice())
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public List<ProductResponse.ProductListDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse.ProductListDto> productList = new ArrayList<>();

        if (products.isEmpty()) {
            throw new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        for (Product product : products) {
            Long brand_id = product.getBrand().getId();
            Optional<Brand> brand = brandRepository.findById(brand_id);
            String brand_name = brand.map(Brand::getBrandName).orElse(null); // 상품 브랜드 존재 하지 않을 시 null 처리
            ProductResponse.ProductListDto product_dto = ProductConverter.toProductListDto(product, brand_name);
            productList.add(product_dto);
        }
        return productList;
    }

    public Product uploadProduct(ProductRequest.ProductUploadDto productUploadDto) {
        if (productUploadDto.getName() == null || productUploadDto.getName().isEmpty() ||
                productUploadDto.getProduct_image() == null || productUploadDto.getProduct_image().isEmpty() || productUploadDto.getPrice() == null) {
            throw new ProductHandler(ErrorStatus.PRODUCT_BAD_REQUEST);
        }
        Product new_product = ProductConverter.toProduct(productUploadDto);
        Brand brand = brandRepository.findById(productUploadDto.getBrand_id())
                .orElseThrow(() -> new ProductHandler(ErrorStatus.BRAND_NOT_FOUND));
        new_product.setBrand(brand);
        return productRepository.save(new_product);
    }

    @Transactional
    public ProductResponse.ProductUpdateResponseDTO updateProduct(Long id, ProductRequest.ProductUpdateRequestDTO request) {
        // 1. 상품 조회
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        // 2. 브랜드 변경이 있을 경우, 브랜드 찾기
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(BrandNotFoundException::new);
            product.setBrand(brand);  // ✅ 브랜드 정보 업데이트
        }

        // 3. 상품 정보 업데이트
        product.update(
                request.getName(),
                request.getProductImage(),
                request.getDetailImage(),
                request.getDescription(),
                request.getPrice()
        );

        // 4. 응답 DTO 반환
        return ProductResponse.ProductUpdateResponseDTO.builder()
                .name(product.getProductName())
                .productImage(product.getProductImage())
                .detailImage(product.getDetailImage())
                .description(product.getProductDescription())
                .price(product.getPrice())
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Transactional
    public ProductResponse.ProductDeleteResponseDTO deleteProduct(Long id) {
        // 1. 상품 조회 (없으면 예외 발생)
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        // 2. 삭제 수행
        productRepository.delete(product);

        // 3. 삭제된 상품 정보 반환
        return ProductResponse.ProductDeleteResponseDTO.builder()
                .name(product.getProductName())
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
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
}
