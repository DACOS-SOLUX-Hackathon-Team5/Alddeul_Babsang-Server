package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.MapService;
import com.example.alddeul_babsang.service.StoreService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@AllArgsConstructor
public class MapController {

    private final MapService mapService;

    // 착한 업소 리스트 조회
    @GetMapping("/stores")
    @Operation(summary = "지도 - 착한 업소 리스트 API", description = "전체 가게")
    public ApiResponse<List<StoreDTO.MapStore>> getMapStores() {
        return ApiResponse.onSuccess(mapService.getMapStoreList());
    }

    // 착한 업소 정보 조회
    @PostMapping("/stores/{id}")
    @Operation(summary = "지도 - 착한 업소 정보 API", description = "전체 가게 중 하나 클릭")
    public ApiResponse<StoreDTO.StoreInfo> getMapStore(@PathVariable Long id,
                                                       @RequestParam Long userId) {
        System.out.println("컨트롤러 단의 userId: " + userId);
        return ApiResponse.onSuccess(mapService.getStore(id, userId));
    }
}
