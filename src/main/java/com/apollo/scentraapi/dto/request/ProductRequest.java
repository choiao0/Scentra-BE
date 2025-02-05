package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class ProductRequest {
    @Getter
    public static class ProductUploadDto {
        String name;
        String product_image;
        String detail_image;
        String description;
        Double price;
        Long brand_id;
    }

    @Getter
    public static class ProductUpdateRequestDTO {
        String name;
        String productImage;
        String detailImage;
        String description;
        Double price;
        Long brandId;
    }

    @Getter
    public static class CreateBgImgDTO {
        @NotBlank
        String prompt;
    }

    @Getter
    public static class CreateCompositeImgDTO {
        @NotBlank
        String backgroundImageUrl;
        @NotBlank
        String productImageUrl;
    }
}
