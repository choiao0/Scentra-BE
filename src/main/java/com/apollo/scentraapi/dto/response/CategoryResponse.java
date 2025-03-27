package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;

public class CategoryResponse {

    @Builder
    @Getter
    public static class CategoryDto {
        private Long id;
        private String categoryNameKr;
        private String categoryNameEn;
        private String categoryType;
    }

    @Builder
    @Getter
    public static class CategoryNameDto {
        private String categoryNameKr;
        private String categoryNameEn;
    }
}
