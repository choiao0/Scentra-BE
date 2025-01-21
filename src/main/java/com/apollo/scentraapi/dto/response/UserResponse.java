package com.apollo.scentraapi.dto.response;

import lombok.*;

import java.util.UUID;

public class UserResponse {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class UserSignUpResultDTO {
        UUID userId;
    }
}
