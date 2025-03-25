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
    @PostMapping
    public ApiResponse<List<RecommendationResponseDto>> getRecommendation(@RequestParam Long userId) {
        List<RecommendationResponseDto> recommendationStores = recommendService.getRecommendByUser(userId);
        return ApiResponse.onSuccess(recommendationStores);
    }
    @PostMapping("/similar")
    public ApiResponse<List<RecommendationResponseDto>> geNearestRecommendation(@RequestParam Long storeId) {
        List<RecommendationResponseDto> recommendationStores = recommendService.getRecommendNearestStore(storeId);
        return ApiResponse.onSuccess(recommendationStores);
    }

}
