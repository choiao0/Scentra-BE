package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

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

    @Builder
    @Getter
    public static class ProductDto {
        Long productId;
        String name;
        String productImage;
        String detailImage;
        String description;
        Double price;
        Long brandId;
        String brandName;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class ImageDTO {
        String ImageUrl;
    }

    @Builder
    @Getter
    public static class ProductUpdateResponseDTO {
        String name;
        String productImage;
        String detailImage;
        String description;
        Double price;
        Long brandId;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class ProductDeleteResponseDTO {
        Long productId;
        String name;
        Long brandId;
        String brandName;
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
