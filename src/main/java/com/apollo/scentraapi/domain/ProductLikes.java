package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductLikes extends BaseEntity {

    @Id
    @Column(name="like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column(name="user_id")
    private UUID userId;

    @Column(name="product_id")
    private Long productId;
}
