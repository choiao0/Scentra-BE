package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.dto.request.CategoryRequest;
import com.apollo.scentraapi.dto.response.CategoryResponse;
import com.apollo.scentraapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "**카테고리타입**은 TYPE or NOTE or MOOD로 입력해주세요.")
    public ApiResponse<CategoryResponse.CategoryDto> createCategory(CategoryRequest.CategoryNameDto categoryDto) {
        CategoryResponse.CategoryDto response = categoryService.createCategory(categoryDto);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 반환")
    public ApiResponse<Map<String, List<CategoryResponse.CategoryDto>>> getCategories() {
        List<CategoryResponse.CategoryDto> categories = categoryService.getAllCategories();

        Map<String, List<CategoryResponse.CategoryDto>> response = categories.stream()
                .collect(Collectors.groupingBy(CategoryResponse.CategoryDto::getCategoryType));

        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{category-id}")
    @Operation(summary = "카테고리 검색")
    public ApiResponse<CategoryResponse.CategoryNameDto> getCategory(@PathVariable("category-id") Long id) {
        CategoryResponse.CategoryNameDto response = categoryService.getCategoryById(id);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{category-id}")
    @Operation(summary = "카테고리 삭제")
    public ApiResponse<CategoryResponse.CategoryDto> deleteCategory(@PathVariable("category-id") Long id) {
        CategoryResponse.CategoryDto response = categoryService.deleteCategoryById(id);
        return ApiResponse.onSuccess(response);
    }
}
