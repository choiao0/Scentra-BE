package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.service.BrandService;
import com.apollo.scentraapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/{id}")
    @Operation(summary="브랜드 조회")
    public ResponseEntity<ApiResponse<BrandResponse.BrandDto>> getBrand(@PathVariable Long id) {
        BrandResponse.BrandDto response = brandService.getBrand(id);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @GetMapping
    @Operation(summary="브랜드 목록 조회")
    public ResponseEntity<ApiResponse<List<BrandResponse.BrandListDto>>> getAllBrands() {
        List<BrandResponse.BrandListDto> response = brandService.getAllBrands();
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping
    @Operation(summary="브랜드 생성(업로드)")
    public ResponseEntity<ApiResponse<BrandResponse.BrandDto>> uploadProduct(@RequestBody BrandRequest.BrandUploadRequestDTO request) {
        Brand new_brand = brandService.uploadBrand(request);
        BrandResponse.BrandDto response = BrandConverter.toBrandResponse(new_brand);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PutMapping("/{id}")
    @Operation(summary="브랜드 정보 수정")
    public ResponseEntity<ApiResponse<BrandResponse.BrandUpdateResponseDTO>> updateBrand(
            @PathVariable Long id, @RequestBody BrandRequest.BrandUpdateRequestDTO request) {
        BrandResponse.BrandUpdateResponseDTO response = brandService.updateBrand(id, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}
