package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import com.apollo.scentraapi.apiPayload.exception.handler.CartException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Cart;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.CartRequest;
import com.apollo.scentraapi.dto.response.CartResponse;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.CartRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;

    private User testUser;
    private Product productA1;
    private Product productA2;
    private Product productB1;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();

        testUser = createUser("user", "user@example.com", Gender.MALE, "010-0000-0000");
        userRepository.save(testUser);

        Brand brandA = createBrand("브랜드A", "brandA");
        productA1 = createProduct(brandA, "상품A1", "productA1", 150000d);
        productA2 = createProduct(brandA, "상품A2", "productA2", 270000d);

        Brand brandB = createBrand("브랜드B", "brandB");
        productB1 = createProduct(brandB, "상품B1", "productB1", 160000d);

        brandRepository.saveAll(List.of(brandA, brandB));
        productRepository.saveAll(List.of(productA1, productA2, productB1));
    }

    @DisplayName("유저의 장바구니에 존재하는 상품 목록을 조회한다.")
    @Test
    void should_ReturnCartItems_When_CartHasItems() {
        // given
        Cart cartA1 = createCart(testUser, productA1, 1);
        Cart cartB1 = createCart(testUser, productB1, 2);
        cartRepository.saveAll(List.of(cartA1, cartB1));

        // when
        List<CartResponse.CartItemDto> response = cartService.getCartItems(testUser.getId());

        // then
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting("brandNameEn", "productNameEn", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("brandA", "productA1", 1),
                        tuple("brandB", "productB1", 2)
                );
    }

    @DisplayName("장바구니 목록 조회 시 장바구니가 비어있으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CartHasNoItems() {
        // given
        UUID userId = testUser.getId();

        // when, then
        assertThatThrownBy(() -> cartService.getCartItems(userId))
                .isInstanceOf(CartException.class);
    }

    @DisplayName("유저의 장바구니에 새로운 상품을 수량만큼 추가한다.")
    @Test
    void should_AddCartItem_When_ItemDoesNotExistInCart() {
        // given
        int addQuantity = 2;

        CartRequest.CartUpdateDTO request = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(request, "productId", productA1.getId());
        ReflectionTestUtils.setField(request, "quantity", addQuantity);

        // when
        UUID userId = testUser.getId();
        cartService.addCartItem(userId, request);

        // then
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        assertThat(cartItems).hasSize(1);
        Cart item = cartItems.get(0);
        assertThat(item.getQuantity()).isEqualTo(addQuantity);
    }

    @DisplayName("유저의 장바구니에 존재하는 상품의 수량을 추가한다.")
    @Test
    void should_AddItemQuantity_When_ItemAlreadyExistsInCart() {
        // given
        int currentQuantity = 3;
        int addQuantity = 2;

        Cart cartA1 = createCart(testUser, productA2, currentQuantity);
        cartRepository.save(cartA1);

        CartRequest.CartUpdateDTO request = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(request, "productId", productA2.getId());
        ReflectionTestUtils.setField(request, "quantity", addQuantity);

        // when
        UUID userId = testUser.getId();
        cartService.addCartItem(userId, request);

        // then
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        assertThat(cartItems).hasSize(1);
        Cart item = cartItems.get(0);
        assertThat(item.getQuantity()).isEqualTo(currentQuantity + addQuantity);
    }

    @DisplayName("감소 수량이 현재 수량보다 작으면 장바구니의 상품 수량을 감소시킨다.")
    @Test
    void should_DecreaseItemQuantity_When_QuantityIsSufficient() {
        // given
        int currentQuantity = 3;
        int decreaseQuantity = 1;

        Cart cartA1 = createCart(testUser, productA1, currentQuantity);
        cartRepository.save(cartA1);

        CartRequest.CartUpdateDTO request = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(request, "productId", productA1.getId());
        ReflectionTestUtils.setField(request, "quantity", decreaseQuantity);

        // when
        UUID userId = testUser.getId();
        cartService.decreaseCartItem(userId, request);

        // then
        Cart item = cartRepository.findByUserIdAndProductId(userId, productA1.getId())
                .orElseThrow(() -> new AssertionError("장바구니에 상품이 존재해야 합니다."));
        assertThat(item.getQuantity()).isEqualTo(currentQuantity - decreaseQuantity);
    }

    @DisplayName("감소 수량이 현재 수량과 같으면 장바구니에서 상품을 삭제한다.")
    @Test
    void should_DeleteCartItem_When_QuantityBecomesZero() {
        // given
        int currentQuantity = 3;
        int decreaseQuantity = 3;

        Cart cartA1 = createCart(testUser, productA1, currentQuantity);
        cartRepository.save(cartA1);

        CartRequest.CartUpdateDTO request = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(request, "productId", productA1.getId());
        ReflectionTestUtils.setField(request, "quantity", decreaseQuantity);

        // when
        UUID userId = testUser.getId();
        cartService.decreaseCartItem(userId, request);

        // then
        Optional<Cart> item = cartRepository.findByUserIdAndProductId(userId, productA1.getId());
        assertThat(item).isEmpty();
    }

    @DisplayName("감소 수량이 현재 수량보다 크면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UpdateQuantityIsNegative() {
        // given
        int currentQuantity = 3;
        int decreaseQuantity = 5;

        Cart cartA1 = createCart(testUser, productA1, currentQuantity);
        cartRepository.save(cartA1);

        CartRequest.CartUpdateDTO request = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(request, "productId", productA1.getId());
        ReflectionTestUtils.setField(request, "quantity", decreaseQuantity);

        // when, then
        UUID userId = testUser.getId();
        assertThatThrownBy(() -> cartService.decreaseCartItem(userId, request))
                .isInstanceOf(CartException.class);
    }

    @DisplayName("장바구니에 해당 상품이 존재하면 상품을 정상적으로 삭제한다.")
    @Test
    void should_RemoveCartItem_When_ItemExists() {
        // given
        Cart cartA1 = createCart(testUser, productA1, 1);
        cartRepository.save(cartA1);

        CartRequest.CartDeleteDTO request = new CartRequest.CartDeleteDTO();
        ReflectionTestUtils.setField(request, "productId", productA1.getId());

        // when
        UUID userId = testUser.getId();
        cartService.removeCartItem(userId, request);

        // then
        Optional<Cart> item = cartRepository.findByUserIdAndProductId(userId, productA1.getId());
        assertThat(item).isEmpty();
    }

    @DisplayName("상품 감소/삭제 시 장바구니에 해당 상품이 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_CartItemNotFound() {
        // given
        CartRequest.CartUpdateDTO decreaseRequest = new CartRequest.CartUpdateDTO();
        ReflectionTestUtils.setField(decreaseRequest, "productId", productA1.getId());
        ReflectionTestUtils.setField(decreaseRequest, "quantity", 1);

        CartRequest.CartDeleteDTO deleteRequest = new CartRequest.CartDeleteDTO();
        ReflectionTestUtils.setField(deleteRequest, "productId", productA1.getId());

        // when, then
        UUID userId = testUser.getId();
        assertThatThrownBy(() -> cartService.decreaseCartItem(userId, decreaseRequest))
                .isInstanceOf(CartException.class);
        assertThatThrownBy(() -> cartService.removeCartItem(userId, deleteRequest))
                .isInstanceOf(CartException.class);
    }

    private User createUser(String name, String email, Gender gender, String phoneNum){
        return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .phoneNum(phoneNum)
                .build();
    }

    private Cart createCart(User user, Product product, int quantity) {
        return Cart.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();
    }

    private Brand createBrand(String nameKr, String nameEn) {
        return Brand.builder()
                .brandNameKr(nameKr)
                .brandNameEn(nameEn)
                .build();
    }

    private Product createProduct(Brand brand, String nameKr, String nameEn, Double price) {
        return Product.builder()
                .brand(brand)
                .productNameKr(nameKr)
                .productNameEn(nameEn)
                .price(price)
                .build();
    }
}