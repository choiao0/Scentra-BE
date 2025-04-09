package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class BrandResponse {

    @Builder
    @Getter
    public static class BrandDto {
        Long id;
        String brandNameKr;
        String brandNameEn;
        String brandDescription;
        String brandImage;
    }

    @Builder
    @Getter
    public static class BrandListDto {
        Long id;
        String brandNameKr;
        String brandNameEn;
        String brandDescription;
        String brandImage;
    }

    @Builder
    @Getter
    public static class BrandUpdateResponseDTO {
        String brandNameKr;
        String brandNameEn;
        String brandDescription;
        String brandImage;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class BrandDeleteResponseDTO {
        Long id;
        String brandNameKr;
        String brandNameEn;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    public static class BrandLikeDTO {
        Long brandLikeId;
        Long brandId;
    }
}
