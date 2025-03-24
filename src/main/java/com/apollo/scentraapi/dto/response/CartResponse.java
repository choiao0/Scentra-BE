package com.apollo.scentraapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class CartResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemDto {
        private Long cartItemId;  // cart_id (bigint)
        private UUID userId;
        private Long productId;   // 상품 ID
        private int quantity;     // 담긴 개수
        private String productName;
        private String brandNameKr;
        private String brandNameEn;
        private String productImage;
        private LocalDateTime addedDate; // 상품이 추가된 날짜
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartUpdateDto {
        private Long cartItemId;  // cart_id (bigint)
        private UUID userId;
        private Long productId;   // 상품 ID
        private int quantity;     // 담긴 개수
        private LocalDateTime addedDate; // 상품이 추가된 날짜
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartUpdateResponseDTO {
        private UUID userId;
        private Long cartItemId;
        private int quantity;
        private LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartDeleteResponseDTO {
        private UUID userId;
        private Long cartItemId;
        private LocalDateTime deletedAt;
    }
}
