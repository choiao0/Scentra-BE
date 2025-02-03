package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequest {

    @Getter
    public static class UserSignUpDTO {

        String name;
        String password;
        @NotNull
        String email;
        String gender;
    }

    @Getter
    public static class UserUpdateDTO {

        String name;
        String password;
        String email;
        String gender;
    }
}
