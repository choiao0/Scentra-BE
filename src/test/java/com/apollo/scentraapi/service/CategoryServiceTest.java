package com.apollo.scentraapi.service;

import com.apollo.scentraapi.domain.Category;
import com.apollo.scentraapi.domain.enums.CategoryType;
import com.apollo.scentraapi.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("새로운 카테고리를 정상적으로 추가한다.")
    @Test
    void should_CreateCategory_When_CategoryIsNew() {

    }

    @DisplayName("이미 존재하는 카테고리를 추가하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CategoryAlreadyExists () {

    }

    @DisplayName("모든 카테고리 정보를 조회한다.")
    @Test
    void should_GetAllCategories_When_RequestIsValid() {

    }

    @DisplayName("유효한 카테고리 ID로 카테고리 정보를 조회한다.")
    @Test
    void should_GetCategory_When_CategoryIdIsValid() {

    }

    @DisplayName("존재하지 않는 ID로 카테고리를 조회하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CategoryIdIsNotValid() {

    }

    @DisplayName("유효한 카테고리 ID로 카테고리를 삭제한다..")
    @Test
    void should_DeleteCategory_When_CategoryIdIsValid() {

    }

    @DisplayName("존재하지 않는 ID로 카테고리를 삭제하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_DeletingCategoryIdIsNotValid() {

    }

    private Category createCategory(String nameKr, String nameEn, CategoryType type) {
        return Category.builder()
                .categoryNameKr(nameKr)
                .categoryNameEn(nameEn)
                .categoryType(type)
                .build();
    }
}