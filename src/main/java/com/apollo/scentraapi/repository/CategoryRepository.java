package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
