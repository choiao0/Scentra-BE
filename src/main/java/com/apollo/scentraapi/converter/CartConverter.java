package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Cart;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.response.CartResponse;

public class CartConverter {
    public static CartResponse.CartItemDto toCartItemDto(Cart cart) {
        return CartResponse.CartItemDto.builder()
                .cartItemId(cart.getId())
                .userId(cart.getUser().getId())
                .productId(cart.getProduct().getId())
                .quantity(cart.getQuantity())
                .price(cart.getProduct().getPrice())
                .productNameKr(cart.getProduct().getProductNameKr())
                .productNameEn(cart.getProduct().getProductNameEn())
                .brandNameKr(cart.getProduct().getBrand().getBrandNameKr())
                .brandNameEn(cart.getProduct().getBrand().getBrandNameEn())
                .productImage(cart.getProduct().getProductImage())
                .addedDate(cart.getUpdatedAt())
                .build();
    }

    public static CartResponse.CartUpdateDto toCartUpdateDto(Cart cart) {
        return CartResponse.CartUpdateDto.builder()
                .cartItemId(cart.getId())
                .userId(cart.getUser().getId())
                .productId(cart.getProduct().getId())
                .quantity(cart.getQuantity())
                .addedDate(cart.getUpdatedAt())
                .build();
    }

    public static CartResponse.CartDeleteResponseDTO toCartDeleteDto(Cart cart) {
        return CartResponse.CartDeleteResponseDTO.builder()
                .userId(cart.getUser().getId())
                .cartItemId(cart.getId())
                .deletedAt(cart.getUpdatedAt())
                .build();
    }

    public static Cart toCart(User user, Product product) {
        return Cart.builder()
                .user(user)
                .product(product)
                .quantity(0)
                .build();
    }
}
