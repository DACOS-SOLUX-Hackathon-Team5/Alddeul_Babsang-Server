package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.ReviewService;
import com.example.alddeul_babsang.web.dto.ReviewRequestDto;
import com.example.alddeul_babsang.web.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {
    @Autowired
    private final  ReviewService reviewService;
    @PostMapping(consumes =MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ReviewResponseDto> createReview(@ModelAttribute ReviewRequestDto reviewRequestDto
                                                      ) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(reviewRequestDto);
        return ApiResponse.onSuccess(reviewResponseDto);
    }


}
