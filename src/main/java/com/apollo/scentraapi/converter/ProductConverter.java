package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.ProductLikes;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;

import java.time.LocalDateTime;

public class ProductConverter {

    public static ProductResponse.ProductListDto toProductListDto(Product product, String brand_name) {
        return ProductResponse.ProductListDto.builder()
                .product_id(product.getId())
                .brand_name(brand_name)
                .product_name(product.getProductName())
                .product_image(product.getProductImage())
                .price(product.getPrice())
                .build();
    }
    public static Product toProduct (ProductRequest.ProductUploadDto productUploadDto) {
        return Product.builder()
                .productName(productUploadDto.getName())
                .productImage(productUploadDto.getProduct_image())
                .detailImage(productUploadDto.getDetail_image())
                .productDescription(productUploadDto.getDescription())
                .price(productUploadDto.getPrice())
                .build();
    }
    public static ProductResponse.ProductDto toProductResponse(Product product) {
        return ProductResponse.ProductDto.builder()
                .productId(product.getId())
                .name(product.getProductName())
                .productImage(product.getProductImage())
                .detailImage(product.getDetailImage())
                .description(product.getProductDescription())
                .price(product.getPrice())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getBrandName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static ProductResponse.ImageDTO toImageDTO(String url) {
        return ProductResponse.ImageDTO.builder()
                .ImageUrl(url)
                .build();
    }

    public static ProductResponse.ProductUpdateResponseDTO toProductUpdateResponseDTO(Product product) {
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

    public static ProductResponse.ProductDeleteResponseDTO toProductDeleteResponseDTO(Product product) {
        return ProductResponse.ProductDeleteResponseDTO.builder()
                .productId(product.getId())
                .name(product.getProductName())
                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static ProductLikes toProductLike(Product product, User user) {
        return ProductLikes.builder()
                .user(user)
                .product(product)
                .build();
    }

    public static ProductResponse.ProductLikeDTO toProductLikeDTO(ProductLikes productLike) {
        return ProductResponse.ProductLikeDTO.builder()
                .productLikeId(productLike.getId())
                .productId(productLike.getProduct().getId())
                .build();
    }
}
