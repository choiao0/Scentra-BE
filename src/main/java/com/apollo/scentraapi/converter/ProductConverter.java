package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Product;
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
}
