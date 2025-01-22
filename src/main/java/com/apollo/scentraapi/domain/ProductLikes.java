package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ProductLikes {
    @Id
    @Column(name="like_id")
    private Long likeId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="product_id")
    private Long productId;
}
