package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brandNameKr;
    private String brandNameEn;
    private String brandDescription;
    private String brandImage;

    // 상품 정보 업데이트 메서드
    public void update(String brandNameKr, String brandNameEn, String brandImage, String brandDescription) {
        if (brandNameKr != null) this.brandNameKr = brandNameKr;
        if (brandNameEn != null) this.brandNameEn = brandNameEn;
        if (brandImage != null) this.brandImage = brandImage;
        if (brandDescription != null) this.brandDescription = brandDescription;
    }
}
