package com.apollo.scentraapi.dto.request;

import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class CategoryNameDto {
        String categoryNameKr;
        String categoryNameEn;
        String categoryType;
    }
}
