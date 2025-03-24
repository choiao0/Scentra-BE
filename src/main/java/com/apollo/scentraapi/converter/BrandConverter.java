package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;


public class BrandConverter {
    public static Brand toBrand(UserRequest.SellerSignUpDTO request) {
        return Brand.builder()
                .brandName(request.getBrandNameEn())
                .brandImage(request.getBrandImage())
                .brandDescription(request.getBrandDescription())
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
