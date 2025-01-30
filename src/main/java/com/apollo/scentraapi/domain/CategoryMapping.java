package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryMapping extends BaseEntity {

    @Id
    @Column(name="category_mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryMappingId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="category_id")
    private Long categoryId;
}
