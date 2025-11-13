package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class ProductException extends GeneralException {
    public ProductException(ErrorStatus errorCode) {
        super(errorCode);
    }
}
