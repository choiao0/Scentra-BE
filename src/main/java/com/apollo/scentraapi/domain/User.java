package com.apollo.scentraapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    private UUID userId;

    private String name;
    private String email;
    private String password;
    private String gender;

    @Column(name="created_at")
    private LocalDateTime created_at;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
