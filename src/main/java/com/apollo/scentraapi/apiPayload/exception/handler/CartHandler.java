package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.BaseErrorCode;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class CartHandler extends GeneralException {

    public CartHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
