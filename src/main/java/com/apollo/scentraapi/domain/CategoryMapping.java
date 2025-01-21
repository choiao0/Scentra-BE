package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CategoryMapping {
    @Id
    @Column(name="category_mapping_id")
    private Long categoryMappingId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="category_id")
    private Long categoryId;
}
