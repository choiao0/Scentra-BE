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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartResponse.CartItemDto> getCartItems(UUID userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new CartException(ErrorStatus.CART_NOT_FOUND);
        }

        return cartItems.stream()
                .map(cart -> {
                    Optional<Product> optionalProduct = productRepository.findById(cart.getProduct().getId());

                    if (optionalProduct.isEmpty()) {
                        throw new ProductException(ErrorStatus.PRODUCT_NOT_FOUND);
                    }

                    return CartConverter.toCartItemDto(cart);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CartResponse.CartUpdateDto addCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, product.getId())
                .orElseGet(() -> CartConverter.toCart(user, product));

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        Cart savedCart = cartRepository.save(cartItem);

        return CartConverter.toCartUpdateDto(savedCart);
    }

    @Transactional
    public void decreaseCartItem(UUID userId, CartRequest.CartUpdateDTO request) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseThrow(() -> new CartException(ErrorStatus.CART_ITEM_NOT_FOUND));

        int updatedQuantity = cartItem.getQuantity() - request.getQuantity();
        if (updatedQuantity < 0) {
            throw new CartException(ErrorStatus.INVALID_QUANTITY);
        } else if (updatedQuantity == 0) {
            cartRepository.delete(cartItem);
        } else {
            cartItem.updateQuantity(updatedQuantity);
            cartRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeCartItem(UUID userId, CartRequest.CartDeleteDTO request) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseThrow(() -> new CartException(ErrorStatus.CART_ITEM_NOT_FOUND));

        cartRepository.deleteByUserIdAndProductId(userId, request.getProductId());
    }
}
