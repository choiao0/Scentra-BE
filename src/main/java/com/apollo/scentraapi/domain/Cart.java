package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Cart {
    @Id
    private Long cart_id;
    private UUID user_id;
    private Long product_id;
    private Integer quantity;
    private LocalDateTime updated_at;
}
