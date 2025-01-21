package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Brand {
    @Id
    @Column(name="brand_id")
    private Long brandId;

    @Column(name="brand_name")
    private String brandName;

    @Column(name="brand_description")
    private String brandDescription;

    @Column(name="brand_image")
    private String brandImage;
}
