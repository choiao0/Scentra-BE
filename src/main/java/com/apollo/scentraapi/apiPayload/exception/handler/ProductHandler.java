package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.BaseErrorCode;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class ProductHandler extends GeneralException {
    public ProductHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
