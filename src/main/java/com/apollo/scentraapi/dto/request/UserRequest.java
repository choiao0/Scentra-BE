package com.apollo.scentraapi.dto.request;

import lombok.Getter;

public class UserRequest {

    @Getter
    public static class UserSignUpDTO {

        String name;
        String email;
        String password;
        String gender;
    }

}
