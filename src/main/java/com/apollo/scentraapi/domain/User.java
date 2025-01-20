package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class User {
    @Id
    private UUID user_id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
