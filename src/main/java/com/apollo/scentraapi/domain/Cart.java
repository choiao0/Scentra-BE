package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Cart {
    @Id
    @Column(name="cart_id")
    private Long cartId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="product_id")
    private Long productId;

    private Integer quantity;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
