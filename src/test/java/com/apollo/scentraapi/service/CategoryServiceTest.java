package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.enums.CategoryType;
import com.apollo.scentraapi.dto.request.CategoryRequest;
import com.apollo.scentraapi.dto.response.CategoryResponse;
import com.apollo.scentraapi.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanDatabase() {
        categoryRepository.deleteAll();
    }

    @DisplayName("새로운 카테고리를 정상적으로 추가한다.")
    @Test
    void should_CreateCategory_When_CategoryIsNew() {
        // given
        String nameKr = "카테고리A";
        String nameEn = "categoryA";
        String type = "NOTE";

        CategoryRequest.CategoryNameDto request = new CategoryRequest.CategoryNameDto();
        ReflectionTestUtils.setField(request, "categoryNameKr", nameKr);
        ReflectionTestUtils.setField(request, "categoryNameEn", nameEn);
        ReflectionTestUtils.setField(request, "categoryType", type);

        // when
        categoryService.createCategory(request);

        // then
        Category categoryA = categoryRepository.findByCategoryNameKr(nameKr)
                .orElseThrow(() -> new AssertionError("카테고리가 존재해야 합니다."));
        assertThat(categoryA.getCategoryNameEn()).isEqualTo(nameEn);
        assertThat(categoryA.getCategoryType()).isEqualTo(CategoryType.NOTE);
    }

    @DisplayName("이미 존재하는 카테고리를 추가하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CategoryAlreadyExists () {
        // given
        String nameKr = "카테고리A";
        String nameEn = "categoryA";
        String type = "NOTE";

        Category categoryA = createCategory(nameKr, nameEn, CategoryType.NOTE);
        categoryRepository.save(categoryA);

        CategoryRequest.CategoryNameDto request = new CategoryRequest.CategoryNameDto();
        ReflectionTestUtils.setField(request, "categoryNameKr", nameKr);
        ReflectionTestUtils.setField(request, "categoryNameEn", nameEn);
        ReflectionTestUtils.setField(request, "categoryType", type);

        // when, then
        assertThatThrownBy(() -> categoryService.createCategory(request))
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("모든 카테고리 정보를 조회한다.")
    @Test
    void should_GetAllCategories_When_RequestIsValid() {
        // given
        Category categoryA = createCategory("카테고리A", "categoryA", CategoryType.TYPE);
        Category categoryB = createCategory("카테고리B", "categoryB", CategoryType.NOTE);
        Category categoryC = createCategory("카테고리C", "categoryC", CategoryType.MOOD);
        categoryRepository.saveAll(List.of(categoryA, categoryB, categoryC));

        // when
        List<CategoryResponse.CategoryDto> response = categoryService.getAllCategories();

        // then
        assertThat(response).hasSize(3);
        assertThat(response)
                .extracting("categoryNameEn", "categoryType")
                .containsExactlyInAnyOrder(
                        tuple("categoryA", "TYPE"),
                        tuple("categoryB", "NOTE"),
                        tuple("categoryC", "MOOD")
                        );
    }

    @DisplayName("유효한 카테고리 ID로 카테고리 정보를 조회한다.")
    @Test
    void should_GetCategory_When_CategoryIdIsValid() {
        // given
        Category categoryA = createCategory("카테고리A", "categoryA", CategoryType.NOTE);
        categoryRepository.save(categoryA);

        // when
        Long categoryId = categoryA.getId();
        CategoryResponse.CategoryNameDto response = categoryService.getCategoryById(categoryId);

        // then
        assertThat(response.getCategoryNameKr()).isEqualTo("카테고리A");
        assertThat(response.getCategoryNameEn()).isEqualTo("categoryA");
    }

    @DisplayName("유효한 카테고리 ID로 카테고리를 삭제한다.")
    @Test
    void should_DeleteCategory_When_CategoryIdIsValid() {
        // given
        Category categoryA = createCategory("카테고리A", "categoryA", CategoryType.NOTE);
        categoryRepository.save(categoryA);

        // when
        Long categoryId = categoryA.getId();
        categoryService.deleteCategoryById(categoryId);

        // then
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).isEmpty();
    }

    @DisplayName("존재하지 않는 ID로 카테고리를 조회/삭제하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CategoryIdIsNotValid() {
        // given
        Long invalidId = 999L;

        // when, then
        assertThatThrownBy(() -> categoryService.getCategoryById(invalidId))
                .isInstanceOf(ProductException.class);
        assertThatThrownBy(() -> categoryService.deleteCategoryById(invalidId))
                .isInstanceOf(ProductException.class);
    }

    private Category createCategory(String nameKr, String nameEn, CategoryType type) {
        return Category.builder()
                .categoryNameKr(nameKr)
                .categoryNameEn(nameEn)
                .categoryType(type)
                .build();
    }
}