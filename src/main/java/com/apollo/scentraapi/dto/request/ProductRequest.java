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
    }

    @Getter
    public static class CreateBgImgDTO {
        @NotBlank
        String prompt;
    }
}
