package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.ProductException;
import com.apollo.scentraapi.apiPayload.exception.handler.UserException;
import com.apollo.scentraapi.converter.ReviewConverter;
import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.ReviewRequest;
import com.apollo.scentraapi.dto.response.ReviewResponse;
import com.apollo.scentraapi.repository.ProductRepository;
import com.apollo.scentraapi.repository.ReviewRepository;
import com.apollo.scentraapi.repository.UserRepository;
import java.util.UUID;
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
    public ReviewResponse.ReviewResultDTO createReview(User user, Long productId, ReviewRequest.ReviewCreateDTO request) {
        User findUser = getUserOrThrow(user.getId());
        Product findProduct = getProductOrThrow(productId);

        Review newReview = ReviewConverter.toReview(findProduct, request);
        newReview.setUser(findUser);

        Review createdReview = reviewRepository.save(newReview);
        refreshProductRatingStats(findProduct);
        return ReviewConverter.toReviewResultDTO(createdReview);
    }

    @Transactional
    public ReviewResponse.ReviewResultDTO updateReview(User user, Long reviewId, ReviewRequest.ReviewUpdateDTO request) {
        Review findReview = getReviewOrThrow(reviewId);

        if (!findReview.getUser().equals(user)) {
            throw new ProductException(ErrorStatus.REVIEW_OWNER_MISMATCH);
        }

        findReview.update(request.getContent(), request.getRating(), request.getImageUrl());

        Review updatedReview = reviewRepository.save(findReview);
        refreshProductRatingStats(findReview.getProduct());
        return ReviewConverter.toReviewResultDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review findReview = getReviewOrThrow(reviewId);
        User findUser = getUserOrThrow(user.getId());

        if (!findReview.getUser().equals(user)) {
            throw new ProductException(ErrorStatus.REVIEW_OWNER_MISMATCH);
        }

        Product product = findReview.getProduct();
        findUser.getReviewList().remove(findReview);
        reviewRepository.delete(findReview);
        refreshProductRatingStats(product);
    }

    public List<Review> getReviewList(Long productId) {
        Product product = getProductOrThrow(productId);
        return reviewRepository.findAllByProduct(product);
    }

    private Review getReviewOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductException(ErrorStatus.REVIEW_NOT_FOUND));
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorStatus.PRODUCT_NOT_FOUND));
    }

    private void refreshProductRatingStats(Product product) {
        Double avgRating = reviewRepository.findAverageRatingByProduct(product);
        long reviewCount = reviewRepository.countByProduct(product);
        product.updateRatingStats(avgRating, (int) reviewCount);
    }
}
