package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Category {
    @Id
    @Column(name="category_id")
    private Long categoryId;

    @Column(name="category_name")
    private String categoryName;
}
