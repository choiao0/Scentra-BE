package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductHandler;
import com.apollo.scentraapi.apiPayload.exception.handler.UserHandler;
import com.apollo.scentraapi.converter.ReviewConverter;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.ReviewRequest;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.ReviewRepository;
import com.apollo.scentraapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Review createReview(User user, Long productId, ReviewRequest.ReviewCreateDTO request) {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        Review newReview = ReviewConverter.toReview(findProduct, request);
        newReview.setUser(findUser);

        return reviewRepository.save(newReview);
    }

    @Transactional
    public Review updateReview(User user, Long reviewId, ReviewRequest.ReviewUpdateDTO request) {

        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.REVIEW_NOT_FOUND));
        if (!findReview.getUser().getId().equals(user.getId()))
            throw new ProductHandler(ErrorStatus.REVIEW_OWNER_MISMATCH);

        findReview.update(request.getContent(), request.getRating(), request.getImageUrl());

        return reviewRepository.save(findReview);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {

        Review findReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.REVIEW_NOT_FOUND));
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        if (!findReview.getUser().getId().equals(user.getId()))
            throw new ProductHandler(ErrorStatus.REVIEW_OWNER_MISMATCH);

        findUser.getReviewList().remove(findReview);
        reviewRepository.delete(findReview);

        // 확인용 메서드
        findUser.getReviewList().stream()
                .map(Review::getId)  // Review 객체에서 ID만 추출
                .forEach(System.out::println);
    }

    public List<Review> getReviewList(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        return reviewRepository.findAllByProduct(product);
    }
}
