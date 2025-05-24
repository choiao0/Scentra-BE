package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.domain.BrandLikes;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.response.BrandResponse;


public class BrandConverter {
    public static Brand toBrand(UserRequest.SellerSignUpDTO request) {
        return Brand.builder()
                .brandNameKr(request.getBrandNameKr())
                .brandNameEn(request.getBrandNameEn())
                .brandImage(request.getBrandImage())
                .brandDescription(request.getBrandDescription())
                .build();
    }

    public static BrandResponse.BrandDto toBrandResponse(Brand brand) {
        return BrandResponse.BrandDto.builder()
                .id(brand.getId())
                .brandNameKr(brand.getBrandNameKr())
                .brandNameEn(brand.getBrandNameEn())
                .brandImage(brand.getBrandImage())
                .brandDescription(brand.getBrandDescription())
                .build();
    }

    public static BrandResponse.BrandListDto toBrandListDto(Brand brand) {
        return BrandResponse.BrandListDto.builder()
                .id(brand.getId())
                .brandNameKr(brand.getBrandNameKr())
                .brandNameEn(brand.getBrandNameEn())
                .brandImage(brand.getBrandImage())
                .brandDescription(brand.getBrandDescription())
                .build();
    }

    public static BrandResponse.BrandUpdateResponseDTO toBrandUpdateResponseDTO(Brand brand) {
        return BrandResponse.BrandUpdateResponseDTO.builder()
                .brandNameKr(brand.getBrandNameKr())
                .brandNameEn(brand.getBrandNameEn())
                .brandImage(brand.getBrandImage())
                .brandDescription(brand.getBrandDescription())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public static BrandResponse.BrandDeleteResponseDTO toBrandDeleteResponseDTO(Brand brand) {
        return BrandResponse.BrandDeleteResponseDTO.builder()
                .id(brand.getId())
                .brandNameKr(brand.getBrandNameKr())
                .brandNameEn(brand.getBrandNameEn())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public static BrandLikes toBrandLikes(Brand brand, User user) {
        return BrandLikes.builder()
                .user(user)
                .brand(brand)
                .build();
    }

    public static BrandResponse.BrandLikeDTO toBrandLikeDTO(BrandLikes brandLikes) {
        return BrandResponse.BrandLikeDTO.builder()
                .brandLikeId(brandLikes.getId())
                .brandId(brandLikes.getBrand().getId())
                .build();
    }

    public static BrandResponse.RetrieveBrandResponseDTO toRetrieveBrandResponse(Brand brand) {
        return BrandResponse.RetrieveBrandResponseDTO.builder()
                .id(brand.getId())
                .brandNameEn(brand.getBrandNameEn())
                .brandNameKr(brand.getBrandNameKr())
                .build();
    }
}
