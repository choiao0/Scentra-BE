package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Brand extends BaseEntity {

    @Id
    @Column(name="brand_id")
    private Long brandId;

    @Column(name="brand_name")
    private String brandName;

    @Column(name="brand_description")
    private String brandDescription;

    @Column(name="brand_image")
    private String brandImage;
}
