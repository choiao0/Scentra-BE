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

    private Double avgRating;
    private Integer reviewCount;

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

    public void update(String productNameKr, String productNameEn, String productImage,
                       String detailImage, Double price, String targetGender) {
        if (productNameKr != null) {
            this.productNameKr = productNameKr;
        }
        if (productNameEn != null) {
            this.productNameEn = productNameEn;
        }
        if (productImage != null) {
            this.productImage = productImage;
        }
        if (detailImage != null) {
            this.detailImage = detailImage;
        }
        if (price != null) {
            this.price = price;
        }
        if (targetGender != null) {
            this.targetGender = Gender.valueOf(targetGender);
        }
    }

    public void updateRatingStats(Double avgRating, Integer reviewCount) {
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
    }

    public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        return (this.productNameKr != null && this.productNameKr.contains(keyword)) ||  // (1) 키워드가 상품명에 포함됨
                (this.productNameEn != null && this.productNameEn.toLowerCase().contains(lowerKeyword)) ||
                (this.brand != null && (this.brand.getBrandNameKr() != null && this.brand.getBrandNameKr().contains(keyword))) ||  // (2) 키워드가 브랜드명에 포함됨
                (this.brand != null && (this.brand.getBrandNameEn() != null && this.brand.getBrandNameEn().toLowerCase().contains(lowerKeyword)));
    }
}
