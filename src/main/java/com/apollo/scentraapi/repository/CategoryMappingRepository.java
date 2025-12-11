package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.CategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Long> {
    @Query("select cm from CategoryMapping cm " +
            "join fetch cm.product p " +
            "join fetch p.brand " +
            "where cm.category.id = :categoryId")
    List<CategoryMapping> findByCategoryIdWithProductAndBrand(Long categoryId);
}