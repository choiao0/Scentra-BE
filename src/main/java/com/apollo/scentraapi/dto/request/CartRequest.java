package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class CartRequest {

    @Getter
    public static class CartUpdateDTO {
        @NotNull
        private Long productId;
        @Min(1)
        private int quantity;
    }

    @Getter
    public static class CartDeleteDTO {
        @NotNull
        private Long productId;
    }
}
