package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.apollo.scentraapi.apiPayload.exception.handler.UserException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Seller;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.ProductLikesRepository;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.SellerRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductLikesRepository productLikesRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandLikesRepository brandLikesRepository;

    @MockBean
    private S3Service s3Service;

    @BeforeAll
    void setUp() {
        User user = createUser("user", "user@example.com", Gender.MALE, "010-0000-0000");
        User sellerUserA = createUser("sellerA", "sellerA@example.com", Gender.MALE, "010-1111-1111");
        User sellerUserB = createUser("sellerB", "sellerB@example.com", Gender.FEMALE, "010-2222-2222");
        userRepository.saveAll(List.of(user, sellerUserA, sellerUserB));

        Brand brandA = createBrand("브랜드A", "brandA");
        Product productA1 = createProduct(brandA, "상품A1", "productA1", 150000d);
        Product productA2 = createProduct(brandA, "상품A2", "productA2", 270000d);

        Brand brandB = createBrand("브랜드B", "brandB");
        Product productB1 = createProduct(brandB, "상품B1", "productB1", 160000d);

        brandRepository.saveAll(List.of(brandA, brandB));
        productRepository.saveAll(List.of(productA1, productA2, productB1));

        Seller sellerA = createSeller(sellerUserA, brandA);
        Seller sellerB = createSeller(sellerUserB, brandB);
        sellerRepository.saveAll(List.of(sellerA, sellerB));
    }

    @DisplayName("새로운 유저가 회원가입하면 유저가 저장되고 토큰이 생성된다.")
    @Test
    void should_CreateUserAndGenerateToken_When_RequestIsValid() {
        // given
        UserRequest.UserSignUpDTO request = new UserRequest.UserSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "newUser");
        ReflectionTestUtils.setField(request, "email", "newUser@example.com");
        ReflectionTestUtils.setField(request, "gender", "MALE");
        ReflectionTestUtils.setField(request, "phoneNum", "010-3333-3333");

        // when
        UserResponse.UserSignUpResultDTO response = userService.createUser(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getAccessToken()).isNotNull();

        Optional<User> savedUser = userRepository.findByEmail("newUser@example.com");
        assertThat(savedUser).isPresent();
        User newUser = savedUser.get();
        assertThat(newUser.getName()).isEqualTo("newUser");
        assertThat(newUser.getEmail()).isEqualTo("newUser@example.com");
    }

    @DisplayName("이미 존재하는 이메일로 유저가 회원가입하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserEmailExisted() {
        // given
        UserRequest.UserSignUpDTO request = new UserRequest.UserSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "username");
        ReflectionTestUtils.setField(request, "email", "user@example.com");

        // when, then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(UserException.class);
    }

    @DisplayName("새로운 판매자가 회원가입하면 유저와 판매자가 저장되고 토큰이 생성된다.")
    @Test
    void should_CreateSellerAndGenerateToken_When_RequestIsValid() {
        // given
        UserRequest.SellerSignUpDTO request = new UserRequest.SellerSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "newSeller");
        ReflectionTestUtils.setField(request, "email", "newSeller@example.com");
        ReflectionTestUtils.setField(request, "brandNameEn", "newBrand");

        MockMultipartFile brandImage = new MockMultipartFile(
                "brandImage", "logo.png", "image/png", "dummy-image-bytes".getBytes()
        );

        // when
        UserResponse.SellerSignUpResultDTO response = userService.createSeller(brandImage, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getSellerId()).isNotNull();
        assertThat(response.getBrandId()).isNotNull();
        assertThat(response.getAccessToken()).isNotNull();

        Optional<Brand> savedBrand = brandRepository.findByBrandNameEn("newBrand");
        assertThat(savedBrand).isPresent();
        Optional<User> savedUser = userRepository.findByEmail("newSeller@example.com");
        assertThat(savedUser).isPresent();
        Optional<Seller> savedSeller = sellerRepository.findByUser(savedUser.get());
        assertThat(savedSeller).isPresent();

        Seller newSeller = savedSeller.get();

        assertThat(newSeller.getUser().getId()).isEqualTo(savedUser.get().getId());
        assertThat(newSeller.getBrand().getId()).isEqualTo(savedBrand.get().getId());
    }

    @DisplayName("이미 존재하는 이메일로 판매자가 회원가입하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_SellerEmailExisted() {
        // given
        UserRequest.SellerSignUpDTO request = new UserRequest.SellerSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "newSeller");
        ReflectionTestUtils.setField(request, "email", "sellerA@example.com");
        ReflectionTestUtils.setField(request, "brandNameEn", "newBrand");

        MockMultipartFile brandImage = new MockMultipartFile(
                "brandImage", "logo.png", "image/png", "dummy-image-bytes".getBytes()
        );

        // when, then
        assertThatThrownBy(() -> userService.createSeller(brandImage, request))
                .isInstanceOf(UserException.class);
    }

    @DisplayName("유저가 로그인하면 토큰을 반환한다.")
    @Test
    void should_ReturnToken_When_UserLogin() {
        // given
        String email = "user@example.com";

        // when
        UserResponse.LoginResultDTO response = userService.login(email);

        // then
        assertThat(response.getName()).isEqualTo("user");
        assertThat(response.getAccessToken()).isNotNull();
    }

    @DisplayName("존재하지 않는 유저가 로그인을 시도하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_NonExistentUserLogin() {
        // given
        String email = "nonExistenUser@example.com";

        // when, then
        assertThatThrownBy(() -> userService.login(email))
                .isInstanceOf(UserException.class);
    }

    @DisplayName("판매자가 로그인을 시도하면 브랜드, 판매자 정보, 총 상품 수와 토큰을 반환한다.")
    @Test
    void should_ReturnTokenAndSellerInfo_When_SellerLogin() {
        // given
        String sellerEmail = "sellerA@example.com";

        // when
        UserResponse.LoginResultDTO response = userService.login(sellerEmail);

        // then
        assertThat(response.getName()).isEqualTo("sellerA");
        assertThat(response.getBrandNameEn()).isEqualTo("brandA");
        assertThat(response.getTotalProducts()).isEqualTo(2);
        assertThat(response.getAccessToken()).isNotNull();
    }

    @DisplayName("유저 정보 변경 DTO에서 null이 아닌 필드만 유저 정보에 반영된다.")
    @Test
    void should_ChangeUserInfo_When_UpdateUserInfo() {
        // given
        User user = userRepository.findByEmail("user@example.com")
                .orElseThrow(() -> new AssertionError("유저가 DB에 존재해야 합니다."));

        UserRequest.UserUpdateDTO request = new UserRequest.UserUpdateDTO();
        ReflectionTestUtils.setField(request, "phoneNum", "010-4444-4444");

        // when
        userService.updateUser(user, request);

        User updatedUser = userRepository.findByEmail("user@example.com")
                .orElseThrow(() -> new AssertionError("유저가 DB에 존재해야 합니다."));

        // then
        assertThat(updatedUser.getName()).isEqualTo("user");
        assertThat(updatedUser.getGender()).isEqualTo(Gender.MALE);
        assertThat(updatedUser.getPhoneNum()).isEqualTo("010-4444-4444");
    }

    @DisplayName("이미 존재하는 이메일로의 변경을 요청하면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_ChangeEmailToExisting() {
        // given
        User user = userRepository.findByEmail("user@example.com")
                .orElseThrow(() -> new AssertionError("유저가 DB에 존재해야 합니다."));

        UserRequest.UserUpdateDTO request = new UserRequest.UserUpdateDTO();
        ReflectionTestUtils.setField(request, "email", "sellerA@example.com");

        // when, then
        assertThatThrownBy(() -> userService.updateUser(user, request))
                .isInstanceOf(UserException.class);
    }

    @DisplayName("유저가 좋아요한 상품 목록을 브랜드 정보와 함께 반환한다.")
    @Test
    void should_ReturnLikedProductsWithBrandInfo_When_UserHasLikedProducts() {

    }

    @DisplayName("유저가 좋아요한 상품이 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserHasNoLikedProducts() {

    }

    @DisplayName("유저가 좋아요한 상품에 브랜드 정보가 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_LikedProductHasNoBrand() {

    }

    @DisplayName("유저가 좋아요한 브랜드 목록을 반환한다.")
    @Test
    void should_ReturnLikedBrands_When_UserHasLikedBrands() {

    }

    @DisplayName("유저가 좋아요한 브랜드가 없으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserHasNoLikedBrands() {

    }

    private User createUser(String name, String email, Gender gender, String phoneNum){
        return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .phoneNum(phoneNum)
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

    private Seller createSeller(User user, Brand brand) {
        return Seller.builder()
                .user(user)
                .brand(brand)
                .build();
    }
}
