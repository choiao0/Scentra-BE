package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.CartException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.apiPayload.exception.handler.UserException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CartResponse.CartItemDto> getCartItems(UUID userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new CartException(ErrorStatus.CART_NOT_FOUND);
        }

        return cartItems.stream()
                .map(CartConverter::toCartItemDto)
                .toList();
    }

    @Transactional
    public CartResponse.CartUpdateDto addCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Product product = getProductOrThrow(request.getProductId());
        User user = getUserOrThrow(userId);

        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, product.getId())
                .orElseGet(() -> CartConverter.toCart(user, product));

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        Cart savedCart = cartRepository.save(cartItem);

        return CartConverter.toCartUpdateDto(savedCart);
    }

    @Transactional
    public void decreaseCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Cart cartItem = getCartItemOrThrow(userId, request.getProductId());

        int updatedQuantity = cartItem.getQuantity() - request.getQuantity();

        if (updatedQuantity < 0) {
            throw new CartException(ErrorStatus.INVALID_QUANTITY);
        }

        if (updatedQuantity > 0) {
            cartItem.updateQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        } else {
            cartRepository.delete(cartItem);
        }
    }

    @Transactional
    public void removeCartItem(UUID userId, CartRequest.CartDeleteDTO request) {
        Cart cartItem = getCartItemOrThrow(userId, request.getProductId());
        cartRepository.delete(cartItem);
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));
    }

    private Cart getCartItemOrThrow(UUID userId, Long productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new CartException(ErrorStatus.CART_ITEM_NOT_FOUND));
    }
}