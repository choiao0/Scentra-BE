package com.apollo.scentraapi.service;

import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.SellerRepository;
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
class BrandServiceTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandLikesRepository brandLikesRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @DisplayName("존재하는 ID로 브랜드 정보를 정상 조회한다.")
    @Test
    void should_ReturnBrandInfo_When_IdExists() {

    }

    @DisplayName("존재하는 ID로 브랜드를 정상 삭제한다.")
    @Test
    void should_DeleteBrand_When_BrandExists() {

    }

    @Test
    @DisplayName("존재하지 않는 ID로 브랜드 조회/삭제 시 예외가 발생한다.")
    void should_ThrowBrandException_When_BrandDoesNotExist() {

    }

    @DisplayName("브랜드 정보 변경 DTO에서 null이 아닌 필드만 브랜드 정보에 반영된다.")
    @Test
    void should_ChangeBrandInfo_When_UpdateBrandInfo() {

    }

    @DisplayName("유저가 판매자인 경우 브랜드 정보를 반환한다.")
    @Test
    void should_ReturnBrandInfo_When_UserIsSeller() {

    }

    @DisplayName("유저가 판매자가 아닌 경우 예외가 발생한다.")
    @Test
    void should_ThrowException_When_UserIsNotSeller() {

    }

    @DisplayName("브랜드 목록 조회 시 모든 브랜드의 정보를 반환한다.")
    @Test
    void should_ReturnAllBrandInfo_When_BrandExists() {

    }

    @DisplayName("브랜드 목록 조회 시 브랜드가 존재하지 않으면 예외가 발생한다.")
    @Test
    void should_ThrowException_When_BrandDoesNotExist() {

    }

    @DisplayName("브랜드 ID가 유효하면 좋아요 추가가 정상 처리된다")
    @Test
    void should_AddBrandLike_When_BrandIdIsValid() {

    }

    @DisplayName("이미 좋아요한 브랜드에 좋아요를 추가하면 예외가 발생한다")
    @Test
    void should_ThrowException_When_BrandAlreadyLiked() {

    }

    @DisplayName("브랜드 ID가 유효하면 좋아요 삭제가 정상 처리된다")
    @Test
    void should_RemoveLike_When_BrandIdIsValid() {

    }

    @DisplayName("좋아요하지 않은 브랜드에 좋아요를 삭제하면 예외가 발생한다")
    @Test
    void should_ThrowException_When_BrandNotLiked() {

    }

    @DisplayName("브랜드 좋아요 여부가 정상적으로 반환된다.")
    @Test
    void should_ReturnLikeStatus_When_BrandIsLiked() {

    }
}
