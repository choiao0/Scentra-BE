package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Cart;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.dto.response.CartResponse;

public class CartConverter {
    // ✅ 장바구니 조회 시 DTO 변환
    public static CartResponse.CartItemDto toCartItemDto(Cart cart) {
        return CartResponse.CartItemDto.builder()
                .cartItemId(cart.getId())
                .userId(cart.getUser().getId())
                .productId(cart.getProduct().getId())
                .quantity(cart.getQuantity())
                .productName(cart.getProduct().getProductName())
                .brandName(cart.getProduct().getBrand().getBrandName())
                .productImage(cart.getProduct().getProductImage())
                .addedDate(cart.getUpdatedAt())
                .build();
    }
    // ✅ 장바구니 상품 추가 후 DTO 변환
    public static CartResponse.CartUpdateDto toCartUpdateDto(Cart cart) {
        return CartResponse.CartUpdateDto.builder()
                .cartItemId(cart.getId())
                .userId(cart.getUser().getId())
                .productId(cart.getProduct().getId())
                .quantity(cart.getQuantity())
                .addedDate(cart.getUpdatedAt())
                .build();
    }

    // ✅ 장바구니 상품 삭제 후 DTO 변환
    public static CartResponse.CartDeleteResponseDTO toCartDeleteDto(Cart cart) {
        return CartResponse.CartDeleteResponseDTO.builder()
                .userId(cart.getUser().getId())
                .cartItemId(cart.getId())
                .deletedAt(cart.getUpdatedAt())
                .build();
    }
}
