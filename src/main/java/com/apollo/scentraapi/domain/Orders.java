package com.apollo.scentraapi.domain;

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
public class Orders {

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

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
