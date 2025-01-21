package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Orders {
    @Id
    private Long order_id;
    private UUID user_id;
    private Double total_price;
    private LocalDateTime date;
    private String state;
    private String address;
    private Integer product_count;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
