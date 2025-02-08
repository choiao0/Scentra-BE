package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.ProductNotFoundException;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandHandler;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.domain.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apollo.scentraapi.apiPayload.exception.BrandNotFoundException;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional
    public BrandResponse.BrandDto getBrand(Long id) {
        // 1. 브랜드 조회 (없으면 예외 발생)
        Brand brand = brandRepository.findById(id)
                .orElseThrow(BrandNotFoundException::new);

        return BrandResponse.BrandDto.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .brandImage(brand.getBrandImage())
                .brandDescription(brand.getBrandDescription())
                .build();
    }

    public Brand uploadBrand(BrandRequest.BrandUploadRequestDTO brandUploadRequestDto) {
        if (brandUploadRequestDto.getBrandName() == null || brandUploadRequestDto.getBrandName().isEmpty() ||
                brandUploadRequestDto.getBrandDescription() == null || brandUploadRequestDto.getBrandImage().isEmpty()) {
            throw new BrandHandler(ErrorStatus.BRAND_BAD_REQUEST);
        }
        Brand new_brand = BrandConverter.toBrand(brandUploadRequestDto);

        return brandRepository.save(new_brand);
    }
}
