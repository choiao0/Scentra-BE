package com.apollo.scentraapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 상품 정보 업데이트 메서드
    public void update(String name, String productImage,
                              String detailImage, String description, Double price) {
        if (name != null) this.productName = name;
        if (productImage != null) this.productImage = productImage;
        if (detailImage != null) this.detailImage = detailImage;
        if (description != null) this.productDescription = description;
        if (price != null) this.price = price;

        this.updatedAt = LocalDateTime.now();
    }

}
