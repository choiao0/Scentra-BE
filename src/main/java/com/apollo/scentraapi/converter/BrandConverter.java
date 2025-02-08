package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;


public class BrandConverter {
    public static Brand toBrand (BrandRequest.BrandUploadRequestDTO brandUploadRequestDTO) {
        return Brand.builder()
                .brandName(brandUploadRequestDTO.getBrandName())
                .brandImage(brandUploadRequestDTO.getBrandImage())
                .brandDescription(brandUploadRequestDTO.getBrandDescription())
                .build();
    }
    public static BrandResponse.BrandDto toBrandResponse(Brand brand) {
        return BrandResponse.BrandDto.builder()
                .id(brand.getId())
                .build();
    }
    public static BrandResponse.BrandListDto toBrandListDto(Brand brand) {
        return BrandResponse.BrandListDto.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .brandImage(brand.getBrandImage())
                .build();
    }
}
