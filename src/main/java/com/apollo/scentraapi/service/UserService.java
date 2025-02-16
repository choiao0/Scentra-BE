package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.BrandHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.UserHandler;
import com.apollo.scentraapi.auth.JwtUtil;
import com.apollo.scentraapi.converter.BrandConverter;
import com.apollo.scentraapi.converter.ProductConverter;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.*;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.BrandResponse;
import com.apollo.scentraapi.dto.response.ProductResponse;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.repository.BrandLikesRepository;
import com.apollo.scentraapi.repository.BrandRepository;
import com.apollo.scentraapi.repository.ProductLikesRepository;
import com.apollo.scentraapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ProductLikesRepository productLikesRepository;
    private final BrandRepository brandRepository;
    private final BrandLikesRepository brandLikesRepository;

    @Transactional
    public UserResponse.UserSignUpResultDTO createUser(UserRequest.UserSignUpDTO request) {

        Optional<User> findUser = userRepository.findByEmail(request.getEmail()); // 이메일로 유저가 존재하는지 검사

        if (findUser.isPresent())
            throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);

        User newUser = UserConverter.toUser(request);
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());

        return UserConverter.toUserSignUpResult(savedUser, accessToken);
    }

    public UserResponse.UserSignUpResultDTO login(String email) {

        User findUser = userRepository.findByEmail(email) // 이메일로 유저가 존재하는지 검사
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String accessToken = jwtUtil.createAccessToken(email);

        return UserConverter.toUserSignUpResult(findUser, accessToken);
    }

    public User updateUser(User user, UserRequest.UserUpdateDTO request) {

        // 이메일 중복 검사
        if (request.getEmail() != null) {
            Optional<User> findUser = userRepository.findByEmail(request.getEmail());

            if (findUser.isPresent())
                throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
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
            throw new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        for (ProductLikes like : likes) {
            Product product = like.getProduct();
            Optional<Brand> brand = brandRepository.findById(product.getBrand().getId());
            String brand_name = brand.map(Brand::getBrandName).orElse(null); // 상품 브랜드 존재 하지 않을 시 null 처리
            ProductResponse.ProductListDto product_dto = ProductConverter.toProductListDto(product, brand_name);
            productList.add(product_dto);
        }
        return productList;
    }

    public List<BrandResponse.BrandListDto> getLikesBrand(User user) {
        List<BrandLikes> likes = brandLikesRepository.findAllByUser(user);
        List<BrandResponse.BrandListDto> brandList = new ArrayList<>();

        if (likes.isEmpty()) {
            throw new BrandHandler(ErrorStatus.BRAND_NOT_FOUND);
        }

        for (BrandLikes like : likes) {
            Brand brand = like.getBrand();
            BrandResponse.BrandListDto brand_dto = BrandConverter.toBrandListDto(brand);
            brandList.add(brand_dto);
        }
        return brandList;
    }
}
