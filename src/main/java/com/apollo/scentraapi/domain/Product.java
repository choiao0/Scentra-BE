package com.apollo.scentraapi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Product {
    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @Column(name="brand_id")
    private Long brandId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_image")
    private String productImage;

    @Column(name="detail_image")
    private String detailImage;

    @Column(name="product_description")
    private String productDescription;

    private Double price;
}
