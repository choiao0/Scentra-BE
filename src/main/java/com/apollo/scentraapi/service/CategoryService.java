package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.converter.CategoryConverter;
import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.dto.request.CategoryRequest;
import com.apollo.scentraapi.dto.response.CategoryResponse;
import com.apollo.scentraapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse.CategoryDto createCategory(CategoryRequest.CategoryNameDto categoryDto) {
        Category newCategory = CategoryConverter.toCategory(categoryDto);
        categoryRepository.save(newCategory);
        return CategoryConverter.toCategoryResponse(newCategory);
    }

    public List<CategoryResponse.CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(CategoryConverter::toCategoryResponse)
                .toList();
    }

    public CategoryResponse.CategoryNameDto getCategoryById(Long categoryId) {
        Category category = getCategoryOrThrow(categoryId);
        return CategoryConverter.toCategoryNameResponse(category);
    }

    public CategoryResponse.CategoryDto deleteCategoryById(Long categoryId) {
        Category category = getCategoryOrThrow(categoryId);
        categoryRepository.delete(category);
        return CategoryConverter.toCategoryResponse(category);
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ProductException(ErrorStatus.CATEGORY_NOT_FOUND));
    }
}
