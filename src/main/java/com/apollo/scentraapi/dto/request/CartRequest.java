package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.util.UUID;

public class CartRequest {

    @Getter
    public static class CartUpdateDTO {
        @NotNull
        private Long productId;  // ✅ 상품 ID
        @Min(1)
        private int quantity;  // ✅ 최소 수량 1 이상만 허용
    }

    @Getter
    public static class CartDeleteDTO {
        @NotNull
        private Long productId;  // ✅ 상품 ID
    }
}
