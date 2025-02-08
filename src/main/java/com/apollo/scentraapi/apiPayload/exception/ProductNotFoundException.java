package com.apollo.scentraapi.apiPayload.exception;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final String code;
    private final String message;

    public ProductNotFoundException() {
        this.code = ErrorStatus.PRODUCT_NOT_FOUND.getCode();
        this.message = ErrorStatus.PRODUCT_NOT_FOUND.getMessage();
    }
}
