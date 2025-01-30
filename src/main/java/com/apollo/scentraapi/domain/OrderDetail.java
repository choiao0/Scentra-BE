package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDetail extends BaseEntity {

    @Id
    @Column(name="order_sub_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderSubId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="order_id")
    private Long orderId;

    private Integer quantity;
}
