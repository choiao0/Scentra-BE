package com.apollo.scentraapi.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;

public class BrandRequest {
    @Getter
    public static class BrandUploadRequestDTO {
        Long id;
        String brandName;
        String brandDescription;
        String brandImage;
    }

    @Getter
    public static class BrandUpdateRequestDTO {
        String brandName;
        String brandDescription;
        String brandImage;
    }
}
