package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p join fetch p.brand")
    List<Product> findAllWithBrand();
    Long countByBrand(Brand brand);
}
