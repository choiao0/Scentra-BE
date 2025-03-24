package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

public class UserRequest {

    @Getter
    public static class UserSignUpDTO {

        String name;
        String password;
        @NotNull
        String email;
        String gender;
        String phoneNum;
        LocalDate birth;
    }

    @Getter
    public static class SellerSignUpDTO {

        String name;
        String password;
        @NotNull
        String email;
        String phoneNum;
        String brandNameKr;
        String brandNameEn;
        String brandImage;
        String brandDescription;
    }

    @Getter
    public static class UserUpdateDTO {

        String name;
        String password;
        String email;
        String gender;
    }
}
