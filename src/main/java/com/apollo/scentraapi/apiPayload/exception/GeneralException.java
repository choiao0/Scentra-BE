package com.apollo.scentraapi.apiPayload.exception;

import com.apollo.scentraapi.apiPayload.code.ReasonDTO;
import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final ErrorStatus code;

    public ReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}