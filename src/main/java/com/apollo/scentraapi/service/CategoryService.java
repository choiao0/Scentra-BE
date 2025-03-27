package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.converter.CategoryConverter;
import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.dto.request.CategoryRequest;
import com.apollo.scentraapi.dto.response.CategoryResponse;
import com.apollo.scentraapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse.CategoryDto createCategory(CategoryRequest.CategoryNameDto category) {
        Category new_category = CategoryConverter.toCategory(category.getCategoryNameKr(), category.getCategoryNameEn(), category.getCategoryType());
        categoryRepository.save(new_category);
        return CategoryConverter.toCategoryResponse(new_category);
    }

    public List<CategoryResponse.CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse.CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse.CategoryDto categoryDto = CategoryConverter.toCategoryResponse(category);
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    public CategoryResponse.CategoryNameDto getCategoryById(Long category_id) {
        Category category = categoryRepository.findById(category_id)
                .orElseThrow(()-> new ProductHandler(ErrorStatus.CATEGORY_NOT_FOUND));
        return CategoryConverter.toCategoryNameResponse(category);
    }

    public CategoryResponse.CategoryDto deleteCategoryById(Long category_id) {
        Category category = categoryRepository.findById(category_id)
                .orElseThrow(()-> new ProductHandler(ErrorStatus.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
        return CategoryConverter.toCategoryResponse(category);
    }
}
