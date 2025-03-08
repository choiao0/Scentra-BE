package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;

public class CategoryResponse {

    @Builder
    @Getter
    public static class CategoryDto {
        private Long id;
        private String name;
    }

    @Builder
    @Getter
    public static class CategoryNameDto {
        private String name;
    }
}
