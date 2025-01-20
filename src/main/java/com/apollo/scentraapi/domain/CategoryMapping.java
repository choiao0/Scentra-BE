package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CategoryMapping {
    @Id
    private Long category_mapping_id;
    private Long product_id;
    private Long category_id;
}
