package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class UserRequest {

    @Getter
    public static class UserSignUpDTO {

        String name;
        @NotNull
        String email;
        String password;
        String gender;
    }

}
