package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.request.ProductRequest;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary="상품 목록 조회")
    public ApiResponse<List<ProductResponse.ProductListDto>> getAllProducts() {
        List<ProductResponse.ProductListDto> response = productService.getAllProducts();
        return ApiResponse.onSuccess(response);
    }

    @PostMapping
    @Operation(summary="상품 업로드")
    public ApiResponse<ProductResponse.ProductDto> uploadProduct(@RequestBody ProductRequest.ProductUploadDto request) {
        Product new_product = productService.uploadProduct(request);
        ProductResponse.ProductDto response = ProductConverter.toProductResponse(new_product);
        return ApiResponse.onSuccess(response);
    }
}
