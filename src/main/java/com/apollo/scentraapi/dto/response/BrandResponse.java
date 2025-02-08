package com.apollo.scentraapi.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class BrandResponse {

    @Builder
    @Getter
    public static class BrandDto {
        Long id;
        String brandName;
        String brandDescription;
        String brandImage;
    }
}
