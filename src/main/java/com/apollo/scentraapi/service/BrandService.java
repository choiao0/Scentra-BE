package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.SellerRepository;
import com.apollo.scentraapi.repository.UserRepository;
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
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    @Transactional
    public BrandResponse.BrandDto getBrand(Long id) {
        // 1. 브랜드 조회 (없으면 예외 발생)
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));

        return BrandConverter.toBrandResponse(brand);
    }

    public BrandResponse.RetrieveBrandResponseDTO retrieveBrand(User user) {
        Seller seller = sellerRepository.findByUser(user)
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
        Brand brand = seller.getBrand();
        return BrandConverter.toRetrieveBrandResponse(brand);
    }

    public List<BrandResponse.BrandListDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandResponse.BrandListDto> brandList = new ArrayList<>();

        if (brands.isEmpty()) {
            throw new ProductException(ErrorStatus.BRAND_NOT_FOUND);
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
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));

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
                .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));

        // 2. 브랜드 삭제 수행
        brandRepository.delete(brand);

        // 3. 삭제된 브랜드 정보 반환
        return BrandConverter.toBrandDeleteResponseDTO(brand);
    }

    public BrandResponse.BrandLikeDTO addLike(User user, Long brandId) {
        Optional<Brand> optionalBrand = brandRepository.findById(brandId);
        Brand brand = optionalBrand.orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));

        Optional<BrandLikes> findBrandLikes = brandLikesRepository.findByUserAndBrand(user, brand);
        if (findBrandLikes.isPresent())
            throw new BrandException(ErrorStatus.BRAND_ALREADY_LIKED);

        BrandLikes newLike = BrandConverter.toBrandLikes(brand, user);
        brandLikesRepository.save(newLike);

        return BrandConverter.toBrandLikeDTO(newLike);
    }

    public BrandResponse.BrandLikeDTO removeLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_LIKED));

        brandLikesRepository.delete(brandLike);
        return BrandConverter.toBrandLikeDTO(brandLike);
    }

    public BrandResponse.BrandLikeDTO isLike(User user, Long brandId) {
        Optional<BrandLikes> optionalBrandLike = brandLikesRepository.findByUserIdAndBrandId(user.getId(), brandId);
        BrandLikes brandLike = optionalBrandLike.orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_LIKED));

        return BrandConverter.toBrandLikeDTO(brandLike);
    }
}