package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Seller {
    @Id
    private UUID admin_id;
    private UUID user_id;
    private Long brand_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
