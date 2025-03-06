package com.apollo.scentraapi.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class ReviewResponse {

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class ReviewResultDTO {
        String username;
        String content;
        Integer rating;
        String imageUrl;
        LocalDate createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListDTO {
        Integer total;
        List<ReviewResultDTO> reviewList;
    }

    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class ReviewDeleteResultDTO {
        Long reviewId;
    }
}
