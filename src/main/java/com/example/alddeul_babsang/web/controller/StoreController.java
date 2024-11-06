package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.service.MapService;
import com.example.alddeul_babsang.service.StoreService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 업소 리스트 조회
    @GetMapping("/stores")
    @Operation(summary = "업소 리스트 API", description = "착한 업소만 조회합니다.")
    public ApiResponse<List<StoreDTO.StoreInfo>> getStores() {
        // Status.Good: 착한 업소
        return ApiResponse.onSuccess(storeService.getStoreList(Status.Good));
    }

    // 업소 상세 조회
    @GetMapping("/stores/{id}")
    @Operation(summary = "업소 상세 조회", description = "store id 입력 - 착한/제보 업소 가능")
    public ApiResponse<StoreDTO.StoreDetail> getStoreDetail(@PathVariable Long id) {
        return ApiResponse.onSuccess(storeService.getStoreInfoDetail(id));
    }
}
