package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class ProductRequest {
    @Getter
    public static class ProductUploadDto {
        @NotBlank
        String productNameKr;
        @NotBlank
        String productNameEn;
        String productImage;
        String detailImage;
        Double price;
        String targetGender;
        @NotNull
        Long brandId;
        List<String> category;
    }

    @Getter
    public static class ProductUpdateRequestDTO {
        String productNameKr;
        String productNameEn;
        String productImage;
        String detailImage;
        Double price;
        String targetGender;
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
