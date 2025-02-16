package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.CategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Long> {
    List<CategoryMapping> findByCategoryId(Long categoryId);
}
