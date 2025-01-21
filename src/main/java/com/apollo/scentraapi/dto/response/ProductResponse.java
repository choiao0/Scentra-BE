package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;

public class ProductResponse {

    @Builder
    @Getter
    public static class ProductListDto {
        Long product_id;
        String brand_name;
        String product_name;
        String product_image;
        double price;
    }
}
