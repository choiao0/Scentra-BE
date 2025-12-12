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
        if (existUser(request.getEmail())) {
            throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
        }

        User newUser = UserConverter.toUser(request);
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());

        return UserConverter.toUserSignUpResult(savedUser, accessToken);
    }

    @Transactional
    public UserResponse.SellerSignUpResultDTO createSeller(MultipartFile brandImage, UserRequest.SellerSignUpDTO request) {
        if (existUser(request.getEmail())) {
            throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
        }

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

    @Transactional(readOnly = true)
    public UserResponse.LoginResultDTO login(String email) {
        User findUser = getUserOrThrow(email);
        Seller findSeller = sellerRepository.findByUserWithBrand(findUser).orElse(null);

        Long totalProducts = null;
        if (findSeller != null) {
            totalProducts = productRepository.countByBrand(findSeller.getBrand());
        }

        String accessToken = jwtUtil.createAccessToken(email);

        return UserConverter.toLoginResult(findUser, findSeller, totalProducts, accessToken);
    }

    public UserResponse.UserInfoResultDTO updateUser(User user, UserRequest.UserUpdateDTO request) {
        String email = request.getEmail();

        if (email != null && existUser(email)) {
            throw new UserException(ErrorStatus.USER_ALREADY_EXIST);
        }

        user.update(request.getName(), request.getPassword(), request.getEmail(), request.getPhoneNum(), request.getGender());
        User updatedUser = userRepository.save(user);

        return UserConverter.toUserInfoResult(updatedUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse.ProductListDto> getLikesProducts(User user) {
        List<ProductLikes> likes = productLikesRepository.findAllByUser(user);

        if (likes.isEmpty()) {
            throw new ProductException(ErrorStatus.NO_LIKED_PRODUCTS);
        }

        List<ProductResponse.ProductListDto> productList = new ArrayList<>();

        for (ProductLikes like : likes) {
            Product product = like.getProduct();
            Brand brand = product.getBrand();
            String brandNameKr = brand.getBrandNameKr();
            String brandNameEn = brand.getBrandNameEn();
            ProductResponse.ProductListDto productDto = ProductConverter.toProductListDto(product, brandNameKr, brandNameEn);
            productList.add(productDto);
        }
        return productList;
    }

    @Transactional(readOnly = true)
    public List<BrandResponse.BrandListDto> getLikesBrand(User user) {
        List<BrandLikes> likes = brandLikesRepository.findAllByUser(user);

        if (likes.isEmpty()) {
            throw new BrandException(ErrorStatus.NO_LIKED_BRANDS);
        }

        List<BrandResponse.BrandListDto> brandList = new ArrayList<>();

        for (BrandLikes like : likes) {
            Brand brand = like.getBrand();
            BrandResponse.BrandListDto brandDto = BrandConverter.toBrandListDto(brand);
            brandList.add(brandDto);
        }
        return brandList;
    }

    private User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    private boolean existUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
