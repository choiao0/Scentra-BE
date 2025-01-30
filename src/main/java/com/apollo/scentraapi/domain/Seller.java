package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seller extends BaseEntity {

    @Id
    @Column(name="admin_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="brand_id")
    private Long brandId;

}
