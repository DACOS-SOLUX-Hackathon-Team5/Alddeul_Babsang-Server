package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.RecommendService;
import com.example.alddeul_babsang.web.dto.RecommendRequestDto;
import com.example.alddeul_babsang.web.dto.RecommendationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/recommend")
@RequiredArgsConstructor
@RestController
public class RecommendController {
    private final RecommendService recommendService;

    //가게 추천
    @GetMapping("/preference/{userId}")
    public ApiResponse<List<RecommendationResponseDto>> getRecommendation(@PathVariable Long userId) {
        List<RecommendationResponseDto> recommendationStores = recommendService.getRecommendByUser(userId);
        return ApiResponse.onSuccess(recommendationStores);
    }

    //가까운 가게 추천
    @GetMapping("/nearby/{storeId}")
    public ApiResponse<List<RecommendationResponseDto>> geNearestRecommendation(@PathVariable Long storeId) {
        List<RecommendationResponseDto> recommendationStores = recommendService.getRecommendNearestStore(storeId);
        return ApiResponse.onSuccess(recommendationStores);
    }

}
