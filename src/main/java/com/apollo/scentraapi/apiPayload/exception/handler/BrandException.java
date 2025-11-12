package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class BrandException extends GeneralException {
    public BrandException(ErrorStatus errorCode) {
        super(errorCode);
    }
}
