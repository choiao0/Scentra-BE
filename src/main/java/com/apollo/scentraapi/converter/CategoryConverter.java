package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.CategoryMapping;
import com.apollo.scentraapi.domain.Product;

public class CategoryConverter {
    public static CategoryMapping toCategoryMapping(Category category, Product product) {
        return CategoryMapping.builder()
                .category(category)
                .product(product).build();
    }
}
