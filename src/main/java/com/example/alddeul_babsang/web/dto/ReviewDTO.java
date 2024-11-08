package com.example.alddeul_babsang.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewDTO {

    // 업소 리뷰
    @Builder
    @Getter
    public static class StoreReview {
        private final String nickname; // 닉네임
        private final float rate;      // 평점
        private final String content;  // 내용
        private final String image;   // 이미지
        private final LocalDateTime date;  // 작성일자
    }

    // 업소 모든 리뷰
    @Builder
    @Getter
    public static class StoreReviews {
        private final Integer reviewCnt; // 리뷰 수
        private final List<StoreReview> reviewList; // 리뷰 리스트
    }
}
