package com.apollo.scentraapi.dto.request;

import lombok.Builder;
import lombok.Getter;

public class CategoryRequest {

    @Builder
    @Getter
    public static class CategoryNameDto {
        private String categoryNameKr;
        private String categoryNameEn;
        private String categoryType;
    }
}
