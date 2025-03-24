package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        return BrandConverter.toBrandResponse(brand);
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

    @Transactional
    public BrandResponse.BrandUpdateResponseDTO updateBrand(Long id, BrandRequest.BrandUpdateRequestDTO request) {
        // 1. 브랜드 조회
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        // 2. 브랜드 정보 업데이트
        brand.update(
                request.getBrandNameKr(),
                request.getBrandNameEn(),
                request.getBrandImage(),
                request.getBrandDescription()
        );

        // 3. 응답 DTO 반환
        return BrandConverter.toBrandUpdateResponseDTO(brand);
    }

    @Transactional
    public BrandResponse.BrandDeleteResponseDTO deleteBrand(Long id) {
        // 1. 브랜드 조회
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        // 2. 브랜드 삭제 수행
        brandRepository.delete(brand);

        // 3. 삭제된 브랜드 정보 반환
        return BrandConverter.toBrandDeleteResponseDTO(brand);
    }

    public BrandResponse.BrandLikeDTO addLike(User user, Long brandId) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        Brand brand = optionalBrand.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        BrandLikes newLike = BrandConverter.toBrandLikes(brand, user);
        brandLikesRepository.save(newLike);

        return BrandConverter.toBrandLikeDTO(newLike);
    }

    public BrandResponse.BrandLikeDTO removeLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        brandLikesRepository.delete(brandLike);
        return BrandConverter.toBrandLikeDTO(brandLike);
    }

    public BrandResponse.BrandLikeDTO isLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandHandler(ErrorStatus.BRAND_NOT_FOUND));

        return BrandConverter.toBrandLikeDTO(brandLike);
    }
}