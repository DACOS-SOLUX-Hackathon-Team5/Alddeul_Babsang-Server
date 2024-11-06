package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.MapService;
import com.example.alddeul_babsang.service.StoreService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/map")
@AllArgsConstructor
public class MapController {

    private final MapService mapService;

    // 지도용 착한 업소 리스트 조회
    @GetMapping("/stores")
    @Operation(summary = "지도 - 착한밥상리스트 API", description = "어쩌구")
    public ApiResponse<List<StoreDTO.MapStore>> getMapStores() {
        return ApiResponse.onSuccess(mapService.getMapStoreList());
    }

    // 특정 착한 업소 정보 조회
    @GetMapping("/stores/{id}")
    @Operation(summary = "지도 - 착한밥상리스트 API", description = "어쩌구")
    public ApiResponse<StoreDTO.StoreInfo> getMapStore(@PathVariable int id) {
        return ApiResponse.onSuccess(mapService.getStore(id));
    }
}
