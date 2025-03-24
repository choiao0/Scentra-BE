package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.CartHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.UserHandler;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.Cart;
import com.apollo.scentraapi.dto.request.CartRequest;
import com.apollo.scentraapi.dto.response.CartResponse;
import com.apollo.scentraapi.repository.CartRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.converter.CartConverter;

import com.apollo.scentraapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 장바구니 목록 조회 (상품 정보 포함)
    public List<CartResponse.CartItemDto> getCartItems(UUID userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CartHandler(ErrorStatus.CART_NOT_FOUND);  // ✅ 장바구니 비었을 때 예외 발생
        }

        return cartItems.stream()
                .map(cart -> {
                    Optional<Product> optionalProduct = productRepository.findById(cart.getProduct().getId());

                    if (optionalProduct.isEmpty()) {
                        throw new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND); // ✅ 상품 없을 경우 예외 발생
                    }

                    return CartConverter.toCartItemDto(cart);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CartResponse.CartUpdateDto addCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 장바구니에서 해당 유저의 같은 상품 조회
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, product.getId())
                .orElseGet(() -> CartConverter.toCart(user, product));

        // 기존 상품이면 수량 업데이트, 없으면 새 상품 추가
        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        Cart savedCart = cartRepository.save(cartItem);

        return CartConverter.toCartUpdateDto(savedCart);  // 컨버터에서 변환
    }

    // ✅ 장바구니에서 상품 수량 `1` 감소
    @Transactional
    public void decreaseCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseThrow(() -> new CartHandler(ErrorStatus.CART_ITEM_NOT_FOUND));

        // ✅ **수량이 0이면 삭제, 음수면 예외 발생**
        int updatedQuantity = cartItem.getQuantity() - request.getQuantity();
        if (updatedQuantity < 0) {
            throw new CartHandler(ErrorStatus.INVALID_QUANTITY);
        } if (updatedQuantity <= 0) {
            cartRepository.delete(cartItem);
        } else {
            cartItem.updateQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        }
    }

    // ✅ 장바구니에서 특정 상품 전체 삭제
    @Transactional
    public void removeCartItem(UUID userId, CartRequest.CartDeleteDTO request) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseThrow(() -> new CartHandler(ErrorStatus.CART_ITEM_NOT_FOUND));

        cartRepository.deleteByUserIdAndProductId(userId, request.getProductId()); // ✅ 레포지토리 활용
    }
}
