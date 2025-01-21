package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ProductLikes {
    @Id
    private Long like_id;
    private UUID user_id;
    private Long product_id;
}
