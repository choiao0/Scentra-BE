package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandException;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.apiPayload.exception.handler.UserException;
import com.apollo.scentraapi.auth.JwtUtil;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtUtil jwtUtil;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductLikesRepository productLikesRepository;
    private final BrandRepository brandRepository;
    private final BrandLikesRepository brandLikesRepository;

    @Transactional
    public UserResponse.UserSignUpResultDTO createUser(UserRequest.UserSignUpDTO request) {

        Optional<User> findUser = userRepository.findByEmail(request.getEmail()); // 이메일로 유저가 존재하는지 검사

        if (findUser.isPresent())
            throw new UserException(ErrorStatus.USER_ALREADY_EXIST);

        User newUser = UserConverter.toUser(request);
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());

        return UserConverter.toUserSignUpResult(savedUser, accessToken);
    }

    @Transactional
    public UserResponse.SellerSignUpResultDTO createSeller(MultipartFile brandImage, UserRequest.SellerSignUpDTO request) {

        Optional<User> findUser = userRepository.findByEmail(request.getEmail()); // 이메일로 유저가 존재하는지 검사

        if (findUser.isPresent())
            throw new UserException(ErrorStatus.USER_ALREADY_EXIST);

        User newUser = UserConverter.toUser(request);
        User savedUser = userRepository.save(newUser);

        String brandImageUrl = s3Service.uploadFile(brandImage);

        Brand newBrand = BrandConverter.toBrand(brandImageUrl, request);
        Brand savedBrand = brandRepository.save(newBrand);

        Seller newSeller = UserConverter.toSeller(savedUser, savedBrand);
        Seller savedSeller = sellerRepository.save(newSeller);

        String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());

        return UserConverter.toSellerSignUpResult(savedSeller, accessToken);
    }

    public UserResponse.LoginResultDTO login(String email) {

        User findUser = userRepository.findByEmail(email) // 이메일로 유저가 존재하는지 검사
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        Seller findSeller = sellerRepository.findByUser(findUser).orElse(null);
        Long totalProducts = null;
        if (findSeller != null) {
            totalProducts = productRepository.countByBrand(findSeller.getBrand());
        }

        String accessToken = jwtUtil.createAccessToken(email);

        return UserConverter.toLoginResult(findUser, findSeller, totalProducts, accessToken);
    }

    public User updateUser(User user, UserRequest.UserUpdateDTO request) {

        // 이메일 중복 검사
        if (request.getEmail() != null) {
            Optional<User> findUser = userRepository.findByEmail(request.getEmail());

            if (findUser.isPresent())
                throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
        }

        user.update(request.getName(), request.getPassword(), request.getEmail(), request.getGender());

        // TODO: 이메일 변경시 자동 로그아웃 구현

        return userRepository.save(user);
    }

    public void deleteUser(User user) {

        userRepository.delete(user);
    }

    public List<ProductResponse.ProductListDto> getLikesProducts(User user) {
        List<ProductLikes> likes = productLikesRepository.findAllByUser(user);
        List<ProductResponse.ProductListDto> productList = new ArrayList<>();

        if (likes.isEmpty()) {
            throw new ProductException(ErrorStatus.NO_LIKED_PRODUCTS);
        }

        for (ProductLikes like : likes) {
            Product product = like.getProduct();
            Brand brand = brandRepository.findById(product.getBrand().getId())
                    .orElseThrow(() -> new BrandException(ErrorStatus.BRAND_NOT_FOUND));
            String brandNameKr = brand.getBrandNameKr();
            String brandNameEn = brand.getBrandNameEn();
            ProductResponse.ProductListDto product_dto = ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
            productList.add(product_dto);
        }
        return productList;
    }

    public List<BrandResponse.BrandListDto> getLikesBrand(User user) {
        List<BrandLikes> likes = brandLikesRepository.findAllByUser(user);
        List<BrandResponse.BrandListDto> brandList = new ArrayList<>();

        if (likes.isEmpty()) {
            throw new BrandException(ErrorStatus.NO_LIKED_BRANDS);
        }

        for (BrandLikes like : likes) {
            Brand brand = like.getBrand();
            BrandResponse.BrandListDto brand_dto = BrandConverter.toBrandListDto(brand);
            brandList.add(brand_dto);
        }
        return brandList;
    }
}
