package com.apollo.scentraapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Seller {
    @Id
    @Column(name="admin_id")
    private UUID adminId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="brand_id")
    private Long brandId;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
