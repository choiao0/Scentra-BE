package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.BaseErrorCode;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class BrandHandler extends GeneralException {
    public BrandHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
