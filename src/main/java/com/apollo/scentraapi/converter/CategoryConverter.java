package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.CategoryMapping;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.enums.CategoryType;
import com.apollo.scentraapi.dto.request.CategoryRequest;
import com.apollo.scentraapi.dto.response.CategoryResponse;

public class CategoryConverter {
    public static CategoryMapping toCategoryMapping(Category category, Product product) {
        return CategoryMapping.builder()
                .category(category)
                .product(product)
                .build();
    }

    public static Category toCategory(CategoryRequest.CategoryNameDto categoryDto) {
        return Category.builder()
                .categoryNameKr(categoryDto.getCategoryNameKr())
                .categoryNameEn(categoryDto.getCategoryNameEn())
                .categoryType(CategoryType.valueOf(categoryDto.getCategoryType()))
                .build();
    }

    public static CategoryResponse.CategoryDto toCategoryResponse(Category category) {
        return CategoryResponse.CategoryDto.builder()
                .id(category.getId())
                .categoryNameKr(category.getCategoryNameKr())
                .categoryNameEn(category.getCategoryNameEn())
                .categoryType(String.valueOf(category.getCategoryType()))
                .build();
    }

    public static CategoryResponse.CategoryNameDto toCategoryNameResponse(Category category) {
        return CategoryResponse.CategoryNameDto.builder()
                .categoryNameKr(category.getCategoryNameKr())
                .categoryNameEn(category.getCategoryNameEn())
                .build();
    }
}
