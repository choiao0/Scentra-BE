package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;

public class ProductConverter {

    public static ProductResponse.ProductListDto toProductListDto(Product product, String brand_name) {
        return ProductResponse.ProductListDto.builder()
                .product_id(product.getProductId())
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
                .product_id(product.getProductId())
                .build();
    }

    public static ProductResponse.ImageDTO toImageDTO(String url) {
        return ProductResponse.ImageDTO.builder()
                .ImageUrl(url)
                .build();
    }
}
