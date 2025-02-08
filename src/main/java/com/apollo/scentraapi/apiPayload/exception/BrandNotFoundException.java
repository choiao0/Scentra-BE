package com.apollo.scentraapi.apiPayload.exception;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import lombok.Getter;

@Getter
public class BrandNotFoundException extends RuntimeException {
    private final String code;
    private final String message;

    public BrandNotFoundException() {
        this.code = ErrorStatus.BRAND_NOT_FOUND.getCode();
        this.message = ErrorStatus.BRAND_NOT_FOUND.getMessage();
    }
}