package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/{id}")
    @Operation(summary="브랜드 조회")
    public ApiResponse<BrandResponse.BrandDto> getBrand(@PathVariable Long id) {
        BrandResponse.BrandDto response = brandService.getBrand(id);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary="브랜드 목록 조회")
    public ApiResponse<List<BrandResponse.BrandListDto>> getAllBrands() {
        List<BrandResponse.BrandListDto> response = brandService.getAllBrands();
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/{id}")
    @Operation(summary="브랜드 정보 수정")
    public ApiResponse<BrandResponse.BrandUpdateResponseDTO> updateBrand(
            @PathVariable Long id, @RequestBody BrandRequest.BrandUpdateRequestDTO request) {
        BrandResponse.BrandUpdateResponseDTO response = brandService.updateBrand(id, request);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="브랜드 삭제")
    public ApiResponse<BrandResponse.BrandDeleteResponseDTO> deleteBrand(@PathVariable Long id) {
        BrandResponse.BrandDeleteResponseDTO response = brandService.deleteBrand(id);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/likes/{brand-id}")
    @Operation(summary="브랜드 좋아요 추가")
    public ApiResponse<BrandResponse.BrandLikeDTO> addLike(@AuthenticationPrincipal User user, @PathVariable("brand-id") Long id) {
        BrandResponse.BrandLikeDTO response = brandService.addLike(user, id);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/likes/{brand-id}")
    @Operation(summary="브랜드 좋아요 삭제")
    public ApiResponse<BrandResponse.BrandLikeDTO> removeLike(@AuthenticationPrincipal User user, @PathVariable("brand-id") Long id) {
        BrandResponse.BrandLikeDTO response = brandService.removeLike(user, id);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/likes/{brand-id}")
    @Operation(summary = "브랜드 좋아요 여부 확인")
    public ApiResponse<BrandResponse.BrandLikeDTO> isLike(@AuthenticationPrincipal User user, @PathVariable("brand-id") Long id) {
        BrandResponse.BrandLikeDTO response = brandService.isLike(user, id);
        return ApiResponse.onSuccess(response);
    }

}
