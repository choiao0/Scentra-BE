package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인할 유저의 이메일을 입력해주세요.")
    public ApiResponse<UserResponse.UserSignUpResultDTO> login(@RequestParam String email) {

        UserResponse.UserSignUpResultDTO response = userService.login(email);

        return ApiResponse.onSuccess(response);
    }

    @GetMapping()
    @Operation(summary = "회원 정보 조회")
    public ApiResponse<UserResponse.UserInfoResultDTO> getUserInfo(@AuthenticationPrincipal User user) {

        return ApiResponse.onSuccess(UserConverter.toUserInfoResult(user));
    }

    @PatchMapping()
    @Operation(summary = "회원 정보 수정", description = "수정하지 않을 정보는 null로 입력하세요. <br> 이메일을 수정했다면 다시 로그인해주세요.")
    public ApiResponse<UserResponse.UserInfoResultDTO> updateUser(@AuthenticationPrincipal User user,
                                                                  @RequestBody UserRequest.UserUpdateDTO request) {

        User updatedUser = userService.updateUser(user, request);

        return ApiResponse.onSuccess(UserConverter.toUserInfoResult(updatedUser));
    }

    @DeleteMapping()
    @Operation(summary = "회원 탈퇴")
    public ApiResponse<UserResponse.UserDeleteResultDTO> deleteUser(@AuthenticationPrincipal User user) {

        userService.deleteUser(user);

        return ApiResponse.onSuccess(UserConverter.toUserDeleteResult(user));
    }

    @GetMapping("/likes/products")
    @Operation(summary="상품 좋아요 목록 조회")
    public ApiResponse<List<ProductResponse.ProductListDto>> getLikesProducts(@AuthenticationPrincipal User user) {
        List<ProductResponse.ProductListDto> response = userService.getLikesProducts(user);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/likes/brands")
    @Operation(summary="브랜드 좋아요 목록 조회")
    public ApiResponse<List<BrandResponse.BrandListDto>> getLikesBrands(@AuthenticationPrincipal User user) {
        List<BrandResponse.BrandListDto> response = userService.getLikesBrand(user);
        return ApiResponse.onSuccess(response);
    }
}
