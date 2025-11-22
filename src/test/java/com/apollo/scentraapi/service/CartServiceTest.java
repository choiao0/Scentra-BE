package com.apollo.scentraapi.service;

import com.apollo.scentraapi.repository.CartRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("유저의 장바구니에 존재하는 상품 목록을 조회한다.")
    @Test
    void should_ReturnCartItems_When_CartHasItems() {

    }

    @DisplayName("장바구니 목록 조회 시 장바구니가 비어있으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CartHasNoItems() {

    }

    @DisplayName("유저의 장바구니에 새로운 상품을 수량만큼 추가한다.")
    @Test
    void should_AddCartItem_When_ItemDoesNotExistInCart() {

    }

    @DisplayName("유저의 장바구니에 존재하는 상품의 수량을 추가한다.")
    @Test
    void should_AddItemQuantity_When_ItemAlreadyExistsInCart() {

    }

    @DisplayName("상품 추가 시 유저나 상품의 정보가 잘못된 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserOrProductDoesNotExist() {

    }

    @DisplayName("감소 수량이 현재 수량보다 작으면 장바구니의 상품 수량을 감소시킨다.")
    @Test
    void should_DecreaseItemQuantity_When_QuantityIsSufficient() {

    }

    @DisplayName("감소 수량이 현재 수량과 같으면 장바구니에서 상품을 삭제한다.")
    @Test
    void should_DeleteCartItem_When_QuantityBecomesZero() {

    }

    @DisplayName("감소 수량이 현재 수량보다 크면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UpdateQuantityIsNegative() {

    }

    @DisplayName("장바구니에 해당 상품이 존재하면 상품을 정상적으로 삭제한다.")
    @Test
    void should_RemoveCartItem_When_ItemExists() {

    }

    @DisplayName("상품 감소/삭제 시 장바구니에 해당 상품이 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CartItemNotFound() {

    }
}