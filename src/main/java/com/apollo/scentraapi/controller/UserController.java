package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.service.UserService;
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

    @PostMapping()
    public ApiResponse<UserResponse.UserSignUpResultDTO> createUser(@RequestBody UserRequest.UserSignUpDTO request) {

        User user = userService.createUser(request);
        UserResponse.UserSignUpResultDTO response = UserConverter.toUserSignUpResult(user);

        return ApiResponse.onSuccess(response);
    }
}
