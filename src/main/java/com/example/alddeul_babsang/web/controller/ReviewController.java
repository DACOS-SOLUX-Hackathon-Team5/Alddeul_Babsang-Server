package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {
    @Autowired
    private final  ReviewService reviewService;
    @PostMapping("/{storeId}")
    public ResponseEntity<?> createReview(@PathVariable int storeId,
                                          @RequestParam int userId,
                                          @RequestParam float rating,
                                          @RequestParam String content,
                                          @RequestPart(required = false) MultipartFile reviewImage) {
        reviewService.createReview(storeId, userId, rating, content, reviewImage);
        return ResponseEntity.ok().body("리뷰가 등록되었습니다");
    }


}
