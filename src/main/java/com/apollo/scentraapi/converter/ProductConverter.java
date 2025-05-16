package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.ProductLikes;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;


public class ProductConverter {

    public static ProductResponse.ProductListDto toProductListDto(Product product, String brandNameKr, String brandNameEn) {
        return ProductResponse.ProductListDto.builder()
                .productId(product.getId())
                .brandNameKr(brandNameKr)
                .brandNameEn(brandNameEn)
                .productNameKr(product.getProductNameKr())
                .productNameEn(product.getProductNameEn())
                .productImage(product.getProductImage())
                .price(product.getPrice())
                .targetGender(String.valueOf(product.getTargetGender()))
                .build();
    }
    public static Product toProduct(ProductRequest.ProductUploadDto productUploadDto) {
        return Product.builder()
                .productNameKr(productUploadDto.getProductNameKr())
                .productNameEn(productUploadDto.getProductNameEn())
                .productImage(productUploadDto.getProductImage())
                .detailImage(productUploadDto.getDetailImage())
                .price(productUploadDto.getPrice())
                .targetGender(Gender.valueOf(productUploadDto.getTargetGender()))
                .build();
    }
    public static ProductResponse.ProductDto toProductResponse(Product product) {
        return ProductResponse.ProductDto.builder()
                .productId(product.getId())
                .productNameKr(product.getProductNameKr())
                .productNameEn(product.getProductNameEn())
                .productImage(product.getProductImage())
                .detailImage(product.getDetailImage())
                .price(product.getPrice())
                .targetGender(String.valueOf(product.getTargetGender()))
                .brandId(product.getBrand().getId())
                .brandNameKr(product.getBrand().getBrandNameKr())
                .brandNameEn(product.getBrand().getBrandNameEn())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static ProductResponse.ImageDTO toImageDTO(String url) {
        return ProductResponse.ImageDTO.builder()
                .imageUrl(url)
                .build();
    }

    public static ProductResponse.ProductUpdateResponseDTO toProductUpdateResponseDTO(Product product) {
        return ProductResponse.ProductUpdateResponseDTO.builder()
                .productNameKr(product.getProductNameKr())
                .productNameEn(product.getProductNameEn())
                .productImage(product.getProductImage())
                .detailImage(product.getDetailImage())
                .price(product.getPrice())
                .targetGender(String.valueOf(product.getTargetGender()))
                .brandId(product.getBrand().getId())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public static ProductResponse.ProductDeleteResponseDTO toProductDeleteResponseDTO(Product product) {
        return ProductResponse.ProductDeleteResponseDTO.builder()
                .productId(product.getId())
                .productNameKr(product.getProductNameKr())
                .productNameEn(product.getProductNameEn())
                .brandId(product.getBrand().getId())
                .brandNameKr(product.getBrand().getBrandNameKr())
                .brandNameEn(product.getBrand().getBrandNameEn())
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
