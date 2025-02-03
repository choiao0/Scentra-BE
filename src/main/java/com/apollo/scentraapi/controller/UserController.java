package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "**유저 이메일**은 필수입니다. 중복되지 않도록 입력해주세요. <br> **성별**은 MALE or FEMALE로 입력해주세요.")
    public ApiResponse<UserResponse.UserSignUpResultDTO> createUser(@Valid @RequestBody UserRequest.UserSignUpDTO request) {

        UserResponse.UserSignUpResultDTO response = userService.createUser(request);

        return ApiResponse.onSuccess(response);
    }
}
