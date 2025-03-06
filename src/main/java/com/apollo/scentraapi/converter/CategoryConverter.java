package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.CategoryMapping;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.response.CategoryResponse;

public class CategoryConverter {
    public static CategoryMapping toCategoryMapping(Category category, Product product) {
        return CategoryMapping.builder()
                .category(category)
                .product(product)
                .build();
    }

    public static Category toCategory(String name) {
        return Category.builder()
                .categoryName(name)
                .build();
    }

    public static CategoryResponse.CategoryDto toCategoryResponse(Category category) {
        return CategoryResponse.CategoryDto.builder()
                .id(category.getId())
                .name(category.getCategoryName())
                .build();
    }

    public static CategoryResponse.CategoryNameDto toCategoryNameResponse(Category category) {
        return CategoryResponse.CategoryNameDto.builder()
                .name(category.getCategoryName())
                .build();
    }
}
