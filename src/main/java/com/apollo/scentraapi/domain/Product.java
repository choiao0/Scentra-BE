package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import com.apollo.scentraapi.domain.enums.Gender;
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

    private String productNameKr;
    private String productNameEn;
    private String productImage;
    private String detailImage;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Gender targetGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CategoryMapping> categoryMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductLikes> productLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    public void setBrand(Brand brand){
        this.brand = brand;
    }

    // 상품 정보 업데이트 메서드
    public void update(String productNameKr, String productNameEn, String productImage,
                       String detailImage, Double price, String targetGender) {
        if (productNameKr != null) this.productNameKr = productNameKr;
        if (productNameEn != null) this.productNameEn = productNameEn;
        if (productImage != null) this.productImage = productImage;
        if (detailImage != null) this.detailImage = detailImage;
        if (price != null) this.price = price;
        if (targetGender != null) this.targetGender = Gender.valueOf(targetGender);
    }

}
