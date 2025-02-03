package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_image")
    private String productImage;

    @Column(name="detail_image")
    private String detailImage;

    @Column(name="product_description")
    private String productDescription;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CategoryMapping> categoryMappingList = new ArrayList<>();

    public void setBrand(Brand brand){
        this.brand = brand;
    }

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
