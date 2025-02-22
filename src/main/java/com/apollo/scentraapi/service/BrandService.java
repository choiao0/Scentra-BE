package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.ProductNotFoundException;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apollo.scentraapi.apiPayload.exception.BrandNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandLikesRepository brandLikesRepository;

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

    public List<BrandResponse.BrandListDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandResponse.BrandListDto> brandList = new ArrayList<>();

        if (brands.isEmpty()) {
            throw new ProductHandler(ErrorStatus.BRAND_NOT_FOUND);
        }

        for (Brand brand : brands) {
            BrandResponse.BrandListDto brand_dto = BrandConverter.toBrandListDto(brand);
            brandList.add(brand_dto);
        }
        return brandList;
    }

    public Brand uploadBrand(BrandRequest.BrandUploadRequestDTO brandUploadRequestDto) {
        if (brandUploadRequestDto.getBrandName() == null || brandUploadRequestDto.getBrandName().isEmpty() ||
                brandUploadRequestDto.getBrandDescription() == null || brandUploadRequestDto.getBrandImage().isEmpty()) {
            throw new BrandHandler(ErrorStatus.BRAND_BAD_REQUEST);
        }
        Brand new_brand = BrandConverter.toBrand(brandUploadRequestDto);

        return brandRepository.save(new_brand);
    }

    @Transactional
    public BrandResponse.BrandUpdateResponseDTO updateBrand(Long id, BrandRequest.BrandUpdateRequestDTO request) {
        // 1. 브랜드 조회
        Brand brand = brandRepository.findById(id)
                .orElseThrow(BrandNotFoundException::new);

        // 2. 브랜드 정보 업데이트
        brand.update(
                request.getBrandName(),
                request.getBrandImage(),
                request.getBrandDescription()
        );

        // 3. 응답 DTO 반환
        return BrandResponse.BrandUpdateResponseDTO.builder()
                .brandName(brand.getBrandName())
                .brandImage(brand.getBrandImage())
                .brandDescription(brand.getBrandDescription())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    @Transactional
    public BrandResponse.BrandDeleteResponseDTO deleteBrand(Long id) {
        // 1. 브랜드 조회
        Brand brand = brandRepository.findById(id)
                .orElseThrow(BrandNotFoundException::new);

        // 2. 브랜드 삭제 수행
        brandRepository.delete(brand);

        // 3. 삭제된 브랜드 정보 반환
        return BrandResponse.BrandDeleteResponseDTO.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public BrandResponse.BrandLikeDTO addLike(User user, Long brandId) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);

        Brand brand = optionalBrand.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        BrandLikes newLike = BrandLikes.builder()
                .user(user)
                .brand(brand)
                .build();

        brandLikesRepository.save(newLike);

        return BrandResponse.BrandLikeDTO.builder()
                .brandLikeId(newLike.getId())
                .brandId(newLike.getBrand().getId())
                .build();
    }

    public BrandResponse.BrandLikeDTO removeLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        brandLikesRepository.delete(brandLike);
        return BrandResponse.BrandLikeDTO.builder()
                .brandLikeId(brandLike.getId())
                .brandId(brandLike.getBrand().getId())
                .build();
    }

    public BrandResponse.BrandLikeDTO isLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        return BrandResponse.BrandLikeDTO.builder()
                .brandLikeId(brandLike.getId())
                .brandId(brandLike.getBrand().getId())
                .build();
    }
}