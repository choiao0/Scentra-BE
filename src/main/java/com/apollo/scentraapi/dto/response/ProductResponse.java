package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

public class ProductResponse {

    @Builder
    @Getter
    public static class ProductListDto {
        Long productId;
        String brandNameKr;
        String brandNameEn;
        String productNameKr;
        String productNameEn;
        String productImage;
        double price;
        String targetGender;
    }

    @Builder
    @Getter
    public static class ProductDto {
        Long productId;
        String productNameKr;
        String productNameEn;
        String productImage;
        String detailImage;
        Double price;
        String targetGender;
        Double avgRating;
        Integer reviewCount;
        Long brandId;
        String brandNameKr;
        String brandNameEn;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class ImageDTO {
        String imageUrl;
    }

    @Builder
    @Getter
    public static class ProductUpdateResponseDTO {
        String productNameKr;
        String productNameEn;
        String productImage;
        String detailImage;
        Double price;
        String targetGender;
        Long brandId;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class ProductDeleteResponseDTO {
        Long productId;
        String productNameKr;
        String productNameEn;
        Long brandId;
        String brandNameKr;
        String brandNameEn;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class ProductLikeDTO {
        Long productLikeId;
        Long productId;
    }
}
