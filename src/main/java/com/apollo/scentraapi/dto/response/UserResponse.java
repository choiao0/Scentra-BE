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
        String accessToken;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class SellerSignUpResultDTO {
        UUID userId;
        UUID sellerId;
        Long brandId;
        String accessToken;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class UserInfoResultDTO {
        UUID userId;
        String name;
        String password;
        String email;
        String gender;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class UserDeleteResultDTO {
        UUID userId;
    }
}
