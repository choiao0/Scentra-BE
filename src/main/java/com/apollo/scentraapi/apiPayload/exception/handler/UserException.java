package com.apollo.scentraapi.apiPayload.exception.handler;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {

    public UserException(ErrorStatus errorCode) {
        super(errorCode);
    }
}

