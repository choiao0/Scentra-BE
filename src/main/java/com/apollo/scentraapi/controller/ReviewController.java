package com.apollo.scentraapi.controller;

import com.apollo.scentraapi.apiPayload.ApiResponse;
import com.apollo.scentraapi.converter.ReviewConverter;
import com.apollo.scentraapi.domain.Review;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.ReviewRequest;
import com.apollo.scentraapi.dto.response.ReviewResponse;
import com.apollo.scentraapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    @Operation(summary = "리뷰 생성")
    public ApiResponse<ReviewResponse.ReviewResultDTO> createReview(@AuthenticationPrincipal User user,
                                                                    @RequestParam Long productId,
                                                                    @Valid @RequestBody ReviewRequest.ReviewCreateDTO request) {
        ReviewResponse.ReviewResultDTO response = reviewService.createReview(user, productId, request);
        return ApiResponse.onSuccess(response);
    }

    @PatchMapping("/{review-id}")
    @Operation(summary = "리뷰 수정", description = "수정하지 않을 정보는 null로 입력하세요.")
    public ApiResponse<ReviewResponse.ReviewResultDTO> updateReview(@AuthenticationPrincipal User user,
                                                                    @PathVariable("review-id") Long reviewId,
                                                                    @RequestBody ReviewRequest.ReviewUpdateDTO request) {
        ReviewResponse.ReviewResultDTO response = reviewService.updateReview(user, reviewId, request);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{review-id}")
    @Operation(summary = "리뷰 삭제")
    public ApiResponse<ReviewResponse.ReviewDeleteResultDTO> deleteReview(@AuthenticationPrincipal User user,
                                                                          @PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(user, reviewId);
        return ApiResponse.onSuccess(ReviewConverter.toReviewDeleteResultDTO(reviewId));
    }

    @GetMapping("/{product-id}")
    @Operation(summary = "상품에 대한 리뷰 목록 조회")
    public ApiResponse<ReviewResponse.ReviewListDTO> getReviewList(@PathVariable("product-id") Long productId) {
        List<Review> reviewList = reviewService.getReviewList(productId);
        return ApiResponse.onSuccess(ReviewConverter.toReviewListDTO(reviewList));
    }
}