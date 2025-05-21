package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandNameEn(String brandNameEn);
    Optional<Brand> findByBrandNameKr(String brandNameKr);
}
