package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    private Long product_id;
    private Long brand_id;
    private String product_name;
    private String product_image;
    private String detail_image;
    private String product_description;
    private Double price;
}
