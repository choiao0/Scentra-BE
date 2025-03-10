package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.dto.request.CartRequest;
import com.apollo.scentraapi.dto.response.CartResponse;
import com.apollo.scentraapi.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    @Operation(summary = "유저의 장바구니 목록 조회")
    public ResponseEntity<List<CartResponse.CartItemDto>> getCartItems(@AuthenticationPrincipal User user){
        UUID userId = user.getId();
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @PostMapping
    @Operation(summary="장바구니 상품 추가", description="로그인된 사용자의 장바구니에 상품을 추가합니다.")
    public ApiResponse<CartResponse.CartUpdateDto> addCartItem(@AuthenticationPrincipal User user,
                                                               @Valid @RequestBody CartRequest.CartUpdateDTO request) {
        UUID userId = user.getId();
        CartResponse.CartUpdateDto response = cartService.addCartItem(userId, request);
        return ApiResponse.onSuccess(response);
    }

    // ✅ 장바구니 상품 수량 `1` 감소
    @PatchMapping("/decrease")
    @Operation(summary = "장바구니 상품 수량 감소", description = "상품 수량을 1 감소, 0이면 자동 삭제됨")
    public ApiResponse<Void> decreaseCartItem(@AuthenticationPrincipal User user,
                                              @Valid @RequestBody CartRequest.CartUpdateDTO request) {
        UUID userId = user.getId();
        cartService.decreaseCartItem(userId, request);
        return ApiResponse.onSuccess(null);
    }

    // ✅ 장바구니에서 특정 상품 삭제
    @DeleteMapping
    @Operation(summary = "장바구니 상품 삭제", description = "로그인된 사용자의 장바구니에서 특정 상품을 완전히 삭제합니다.")
    public ApiResponse<Void> removeCartItem(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody CartRequest.CartDeleteDTO request) {
        UUID userId = user.getId();
        cartService.removeCartItem(userId, request);
        return ApiResponse.onSuccess(null);
    }
}