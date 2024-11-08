package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.ReviewService;
import com.example.alddeul_babsang.web.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {
    @Autowired
    private final  ReviewService reviewService;
    @PostMapping("/{storeId}")
    public ApiResponse<ReviewResponseDto> createReview(@PathVariable int storeId,
                                                       @RequestParam int userId,
                                                       @RequestParam float rating,
                                                       @RequestParam String content,
                                                       @RequestPart(required = false) MultipartFile reviewImage) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(storeId, userId, rating, content, reviewImage);
        return ApiResponse.onSuccess(reviewResponseDto);
    }


}
