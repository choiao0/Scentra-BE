package com.apollo.scentraapi.dto.request;

import lombok.Getter;

public class ProductRequest {
    @Getter
    public static class ProductUploadDto {
        String name;
        String product_image;
        String detail_image;
        String description;
        Double price;
    }
}
