package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Brand {
    @Id
    private Long brand_id;
    private String brand_name;
    private String brand_description;
    private String brand_image;
}
