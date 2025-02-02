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

    @Column(name="brand_name")
    private String brandName;

    @Column(name="brand_description")
    private String brandDescription;

    @Column(name="brand_image")
    private String brandImage;
}
