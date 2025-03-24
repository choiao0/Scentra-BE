package com.apollo.scentraapi.dto.request;

import lombok.Getter;

public class BrandRequest {

    @Getter
    public static class BrandUpdateRequestDTO {
        String brandNameKr;
        String brandNameEn;
        String brandDescription;
        String brandImage;
    }
}
