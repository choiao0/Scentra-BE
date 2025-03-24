package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Seller;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;

import java.util.ArrayList;

public class UserConverter {

    public static User toUser(UserRequest.UserSignUpDTO request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .gender(Gender.valueOf(request.getGender()))
                .phoneNum(request.getPhoneNum())
                .birth(request.getBirth())
                .productLikesList(new ArrayList<>())
                .reviewList(new ArrayList<>())
                .cartList(new ArrayList<>())
                .build();
    }

    public static User toUser(UserRequest.SellerSignUpDTO request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNum(request.getPhoneNum())
                .productLikesList(new ArrayList<>())
                .reviewList(new ArrayList<>())
                .cartList(new ArrayList<>())
                .build();
    }
    public static Seller toSeller(User user, Brand brand) {
        return Seller.builder()
                .user(user)
                .brand(brand)
                .build();
    }

    public static UserResponse.UserSignUpResultDTO toUserSignUpResult(User user, String accessToken) {
        return UserResponse.UserSignUpResultDTO.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }

    public static UserResponse.SellerSignUpResultDTO toSellerSignUpResult(Seller seller, String accessToken) {
        return UserResponse.SellerSignUpResultDTO.builder()
                .userId(seller.getUser().getId())
                .sellerId(seller.getId())
                .brandId(seller.getBrand().getId())
                .accessToken(accessToken)
                .build();
    }

    public static UserResponse.UserInfoResultDTO toUserInfoResult(User user) {
        return UserResponse.UserInfoResultDTO.builder()
                .userId(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .gender(String.valueOf(user.getGender()))
                .build();
    }

    public static UserResponse.UserDeleteResultDTO toUserDeleteResult(User user) {
        return UserResponse.UserDeleteResultDTO.builder()
                .userId(user.getId())
                .build();
    }
}