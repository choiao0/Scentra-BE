package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class CartException extends GeneralException {

    public CartException(ErrorStatus errorCode) {
        super(errorCode);
    }
}
