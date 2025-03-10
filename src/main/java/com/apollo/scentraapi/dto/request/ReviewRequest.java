package com.apollo.scentraapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class ReviewRequest {

    @Getter
    public static class ReviewCreateDTO {
        @NotNull
        String content;
        @NotNull
        Integer rating;
        String imageUrl;
    }

    @Getter
    public static class ReviewUpdateDTO {
        String content;
        Integer rating;
        String imageUrl;
    }
}
