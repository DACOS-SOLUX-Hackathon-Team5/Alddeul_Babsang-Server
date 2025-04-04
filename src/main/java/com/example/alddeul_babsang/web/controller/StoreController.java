package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.service.StoreService;
import com.example.alddeul_babsang.web.dto.ReviewDTO;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 업소 리스트 조회
    @PostMapping("/")
    @Operation(summary = "업소 리스트 API", description = "착한 업소만 조회합니다.")
    public ApiResponse<List<StoreDTO.StoreInfo>> getStores(@RequestParam Long userId) {
        System.out.println("컨틀로러 단의 userId 여부: " + userId);

        // Status.Good: 착한 업소
        return ApiResponse.onSuccess(storeService.getStoreList(Status.GOOD, userId));
    }

    // 업소 상세 조회
    @PostMapping("/{storeId}")
    @Operation(summary = "착한/제보 업소 상세 조회", description = "store id 입력 - 착한/제보 업소 가능")
    public ApiResponse<StoreDTO.StoreDetail> getStoreDetail(@PathVariable Long storeId,
                                                            @RequestParam Long Userid) {
        return ApiResponse.onSuccess(storeService.getStoreInfoDetail(storeId, Userid));
    }

    // 업소 리뷰 조회
    @GetMapping("/{id}/reviews")
    @Operation(summary = "착한/제보 업소 리뷰 조회", description = "store id 입력 - 착한/제보 업소 가능")
    public ApiResponse<ReviewDTO.StoreReviews> getStoreReviews(@PathVariable Long id) {
        return ApiResponse.onSuccess(storeService.getReviewList(id));
    }
}
