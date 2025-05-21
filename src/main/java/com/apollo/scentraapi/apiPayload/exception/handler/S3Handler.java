package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.BaseErrorCode;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class S3Handler extends GeneralException {
    public S3Handler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
