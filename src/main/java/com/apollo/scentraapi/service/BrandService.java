package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandLikesRepository brandLikesRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public BrandResponse.BrandDto getBrand(Long id) {
        Brand brand = getBrandOrThrow(id);
        return BrandConverter.toBrandResponse(brand);
    }

    @Transactional(readOnly = true)
    public BrandResponse.RetrieveBrandResponseDTO retrieveBrand(User user) {
        Seller seller = sellerRepository.findByUserWithBrand(user)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
        Brand brand = seller.getBrand();
        return BrandConverter.toRetrieveBrandResponse(brand);
    }

    public List<BrandResponse.BrandListDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new BrandException(ErrorStatus.BRAND_NOT_FOUND);
        }

        return brands.stream()
                .map(BrandConverter::toBrandListDto)
                .toList();
    }

    @Transactional
    public BrandResponse.BrandUpdateResponseDTO updateBrand(Long id, BrandRequest.BrandUpdateRequestDTO request) {
        Brand brand = getBrandOrThrow(id);
        brand.update(
                request.getBrandNameKr(),
                request.getBrandNameEn(),
                request.getBrandImage(),
                request.getBrandDescription()
        );
        return BrandConverter.toBrandUpdateResponseDTO(brand);
    }

    @Transactional
    public BrandResponse.BrandDeleteResponseDTO deleteBrand(Long id) {
        Brand brand = getBrandOrThrow(id);
        brandRepository.delete(brand);
        return BrandConverter.toBrandDeleteResponseDTO(brand);
    }

    public BrandResponse.BrandLikeDTO addLike(User user, Long brandId) {
        Brand brand = getBrandOrThrow(brandId);
        if (existBrandLike(user, brand)) {
            throw new BrandException(ErrorStatus.BRAND_ALREADY_LIKED);
        }

        BrandLikes newLike = BrandConverter.toBrandLikes(brand, user);
        brandLikesRepository.save(newLike);
        return BrandConverter.toBrandLikeDTO(newLike);
    }

    public BrandResponse.BrandLikeDTO removeLike(User user, Long brandId) {
        Brand brand = getBrandOrThrow(brandId);
        BrandLikes brandLike = getBrandLikeOrThrow(user, brand);
        brandLikesRepository.delete(brandLike);
        return BrandConverter.toBrandLikeDTO(brandLike);
    }

    public BrandResponse.BrandLikeDTO isLike(User user, Long brandId) {
        Brand brand = getBrandOrThrow(brandId);
        BrandLikes brandLike = getBrandLikeOrThrow(user, brand);
        return BrandConverter.toBrandLikeDTO(brandLike);
    }

    private Brand getBrandOrThrow(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
    }

    private BrandLikes getBrandLikeOrThrow(User user, Brand brand) {
        return brandLikesRepository.findByUserAndBrand(user, brand)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_LIKED));
    }

    private boolean existBrandLike(User user, Brand brand) {
        return brandLikesRepository.findByUserAndBrand(user, brand).isPresent();
    }
}