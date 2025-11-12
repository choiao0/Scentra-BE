package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class S3Exception extends GeneralException {
    public S3Exception(ErrorStatus errorCode) {
        super(errorCode);
    }
}
