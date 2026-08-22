package com.apollo.scentraapi.converter;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.dto.request.ReviewRequest;
import com.apollo.scentraapi.dto.response.ReviewResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {
    public static Review toReview(Product product, ReviewRequest.ReviewCreateDTO request) {
        return Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .imageUrl(request.getImageUrl())
                .product(product)
                .build();
    }

    public static ReviewResponse.ReviewResultDTO toReviewResultDTO(Review review) {
        return ReviewResponse.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .username(review.getUser().getUsername())
                .content(review.getContent())
                .rating(review.getRating())
                .imageUrl(review.getImageUrl())
                .createdAt(review.getUpdatedAt().toLocalDate())
                .build();
    }

    public static ReviewResponse.ReviewListDTO toReviewListDTO(List<Review> reviewList) {

        List<ReviewResponse.ReviewResultDTO> reviewResultDTOList = reviewList.stream()
                .map(ReviewConverter::toReviewResultDTO).collect(Collectors.toList());

        return ReviewResponse.ReviewListDTO.builder()
                .reviewList(reviewResultDTOList)
                .build();
    }

    public static ReviewResponse.ReviewDeleteResultDTO toReviewDeleteResultDTO(Long reviewId) {
        return ReviewResponse.ReviewDeleteResultDTO.builder()
                .reviewId(reviewId)
                .build();
    }
}
