package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Review {
    @Id
    private Long review_id;
    private Long product_id;
    private UUID user_id;
    private String title;
    private String content;
    private Integer rating;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
