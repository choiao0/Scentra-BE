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
        private Long cartItemId;
        private UUID userId;
        private Long productId;
        private int quantity;
        private Double price;
        private String productNameKr;
        private String productNameEn;
        private String brandNameKr;
        private String brandNameEn;
        private String productImage;
        private LocalDateTime addedDate;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartUpdateDto {
        private Long cartItemId;
        private UUID userId;
        private Long productId;
        private int quantity;
        private LocalDateTime addedDate;
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
