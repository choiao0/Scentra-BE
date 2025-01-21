package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
