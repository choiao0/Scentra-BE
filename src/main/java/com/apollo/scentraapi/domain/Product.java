package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
