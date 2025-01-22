package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDetail {

    @Id
    @Column(name="order_sub_id")
    private Long orderSubId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="order_id")
    private Long orderId;

    private Integer quantity;
}
