package com.apollo.scentraapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.BrandLikes;
import com.apollo.scentraapi.domain.Seller;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.BrandRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.SellerRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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
class BrandServiceTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandLikesRepository brandLikesRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        sellerRepository.deleteAll();
        userRepository.deleteAll();
        brandRepository.deleteAll();
        brandLikesRepository.deleteAll();
    }

    @DisplayName("존재하는 ID로 브랜드 정보를 정상 조회한다.")
    @Test
    void should_ReturnBrandInfo_When_IdExists() {
        // given
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);

        // when
        BrandResponse.BrandDto response = brandService.getBrand(savedBrand.getId());

        // then
        assertThat(response.getBrandNameEn()).isEqualTo("brand");
    }

    @DisplayName("존재하는 ID로 브랜드를 정상 삭제한다.")
    @Test
    void should_DeleteBrand_When_BrandExists() {
        // given
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);

        // when
        brandService.deleteBrand(savedBrand.getId());

        // then
        Optional<Brand> findBrand = brandRepository.findByBrandNameEn("brand");
        assertThat(findBrand).isNotPresent();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 브랜드 조회/삭제 시 예외가 발생한다.")
    void should_ThrowBrandException_When_BrandDoesNotExist() {
        // given
        Long invalidId = 999L;

        // when, then
        assertThatThrownBy(() -> brandService.getBrand(invalidId))
                .isInstanceOf(BrandException.class);
        assertThatThrownBy(() -> brandService.deleteBrand(invalidId))
                .isInstanceOf(BrandException.class);
    }

    @DisplayName("브랜드 정보 변경 DTO에서 null이 아닌 필드만 브랜드 정보에 반영된다.")
    @Test
    void should_ChangeBrandInfo_When_UpdateBrandInfo() {
        // given
        Brand brand = createBrand("브랜드(A)", "brandA");
        Brand savedBrand = brandRepository.save(brand);

        BrandRequest.BrandUpdateRequestDTO request = new BrandRequest.BrandUpdateRequestDTO();
        ReflectionTestUtils.setField(request, "brandNameKr", "브랜드A");

        // when
        brandService.updateBrand(savedBrand.getId(), request);

        // then
        Brand findBrand = brandRepository.findById(savedBrand.getId())
                        .orElseThrow(() -> new AssertionError("브랜드가 DB에 존재해야 합니다."));
        assertThat(findBrand.getBrandNameKr()).isEqualTo("브랜드A");
        assertThat(findBrand.getBrandNameEn()).isEqualTo("brandA");
    }

    @DisplayName("유저가 판매자인 경우 브랜드 정보를 반환한다.")
    @Test
    void should_ReturnBrandInfo_When_UserIsSeller() {
        // given
        User user = createUser("seller", "seller@example.com", Gender.MALE, "010-0000-0000");
        Brand brand = createBrand("브랜드", "brand");
        Seller seller = createSeller(user, brand);
        userRepository.save(user);
        brandRepository.save(brand);
        sellerRepository.save(seller);

        // when
        BrandResponse.RetrieveBrandResponseDTO response = brandService.retrieveBrand(user);

        // then
        assertThat(response.getBrandNameEn()).isEqualTo("brand");
    }

    @DisplayName("유저가 판매자가 아닌 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserIsNotSeller() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-1111-1111");
        userRepository.save(user);

        // when, then
        assertThatThrownBy(() -> brandService.retrieveBrand(user))
                .isInstanceOf(BrandException.class);
    }

    @DisplayName("브랜드 목록 조회 시 모든 브랜드의 정보를 반환한다.")
    @Test
    void should_ReturnAllBrandInfo_When_BrandExists() {
        // given
        Brand brandA = createBrand("브랜드A", "brandA");
        Brand brandB = createBrand("브랜드B", "brandB");
        brandRepository.saveAll(List.of(brandA, brandB));

        // when
        List<BrandResponse.BrandListDto> response = brandService.getAllBrands();

        // then
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting("brandNameEn")
                .containsExactlyInAnyOrder("brandA", "brandB");
    }

    @DisplayName("브랜드 목록 조회 시 브랜드가 존재하지 않으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_BrandDoesNotExist() {
        // given

        // when, then
        assertThatThrownBy(() -> brandService.getAllBrands())
                .isInstanceOf(ProductException.class);
    }

    @DisplayName("브랜드 ID가 유효하면 좋아요 추가가 정상 처리된다")
    @Test
    void should_AddBrandLike_When_BrandIdIsValid() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-2222-2222");
        userRepository.save(user);
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);

        // when
        brandService.addLike(user, savedBrand.getId());

        // then
        List<BrandLikes> brandLikes = brandLikesRepository.findAllByUser(user);
        assertThat(brandLikes).hasSize(1);
    }

    @DisplayName("이미 좋아요한 브랜드에 좋아요를 추가하면 예외가 발생한다")
    @Test
    void should_ThrowException_When_BrandAlreadyLiked() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-2222-2222");
        userRepository.save(user);
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);
        BrandLikes brandLikes = createBrandLikes(user, brand);
        brandLikesRepository.save(brandLikes);

        // when, then
        Long brandId = savedBrand.getId();
        assertThatThrownBy(() -> brandService.addLike(user, brandId))
                .isInstanceOf(BrandException.class);
    }

    @DisplayName("브랜드 ID가 유효하면 좋아요 삭제가 정상 처리된다")
    @Test
    void should_RemoveLike_When_BrandIdIsValid() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-2222-2222");
        userRepository.save(user);
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);
        BrandLikes brandLikes = createBrandLikes(user, brand);
        brandLikesRepository.save(brandLikes);

        // when
        brandService.removeLike(user, savedBrand.getId());

        // then
        List<BrandLikes> findBrandLikes = brandLikesRepository.findAllByUser(user);
        assertThat(findBrandLikes).isEmpty();
    }

    @DisplayName("좋아요하지 않은 브랜드에 좋아요를 삭제하면 예외가 발생한다")
    @Test
    void should_ThrowException_When_BrandNotLiked() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-2222-2222");
        userRepository.save(user);
        Brand brand = createBrand("브랜드", "brand");
        Brand savedBrand = brandRepository.save(brand);

        // when, then
        Long brandId = savedBrand.getId();
        assertThatThrownBy(() -> brandService.removeLike(user, brandId))
                .isInstanceOf(BrandException.class);
    }

    @DisplayName("브랜드 좋아요 여부가 정상적으로 반환된다.")
    @Test
    void should_ReturnLikeStatus_When_BrandIsLiked() {
        // given
        User user = createUser("user", "user@example.com", Gender.MALE, "010-2222-2222");
        userRepository.save(user);
        Brand likedBrand = createBrand("좋아요한 브랜드", "likedBrand");
        Brand notLikedBrand = createBrand("좋아요하지 않은 브랜드", "notLikedBrand");
        brandRepository.saveAll(List.of(likedBrand,notLikedBrand));
        BrandLikes brandLikes = createBrandLikes(user, likedBrand);
        brandLikesRepository.save(brandLikes);

        // when
        BrandResponse.BrandLikeDTO likedResponse = brandService.isLike(user, likedBrand.getId());

        // then
        Long notLikedBrandId = notLikedBrand.getId();
        assertThat(likedResponse.getBrandId()).isEqualTo(likedBrand.getId());
        assertThatThrownBy(() -> brandService.removeLike(user, notLikedBrandId))
                .isInstanceOf(BrandException.class);
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

    private Seller createSeller(User user, Brand brand) {
        return Seller.builder()
                .user(user)
                .brand(brand)
                .build();
    }

    private BrandLikes createBrandLikes(User user, Brand brand) {
        return BrandLikes.builder()
                .user(user)
                .brand(brand)
                .build();
    }
}
