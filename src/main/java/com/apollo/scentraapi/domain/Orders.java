package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Orders extends BaseEntity {

    @Id
    @Column(name="order_id")
    private Long orderId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="total_price")
    private Double totalPrice;

    private LocalDateTime date;

    private String state;

    private String address;

    @Column(name="product_count")
    private Integer productCount;

}
