package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="brand_name")
    private String brandName;

    @Column(name="brand_description")
    private String brandDescription;

    @Column(name="brand_image")
    private String brandImage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 상품 정보 업데이트 메서드
    public void update(String brandName, String brandImage,
                       String brandDescription) {
        if (brandName != null) this.brandName = brandName;
        if (brandImage != null) this.brandImage = brandImage;
        if (brandDescription != null) this.brandDescription = brandDescription;

        this.updatedAt = LocalDateTime.now();
    }
}
