package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.service.CoordinatesService;
import com.example.alddeul_babsang.service.MapService;
import com.example.alddeul_babsang.service.StoreService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@AllArgsConstructor
public class ReportController {

    private final StoreService storeService;

    // 제보된 업소 조회
    @PostMapping("/")
    @Operation(summary = "제보된 업소 조회 API", description = "제보한 업소만 조회합니다.")
    public ApiResponse<List<StoreDTO.StoreInfo>> getStores(@RequestParam Long userId) {
        // Status.PREGOOD: 예비 착한 업소(= 제보된 업소)
        return ApiResponse.onSuccess(storeService.getStoreList(Status.PREGOOD, userId));
    }

    // 업소 제보 등록
    // StoreDTO.StoreReport report
    @Operation(summary = "업소 제보 등록 API", description = "업소를 제보합니다. 바로 착한 업소 등록 x")
    @PostMapping(value="/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> postStore(@RequestPart StoreDTO.StoreReport report) {
        // service 기능 - db 저장,
        return ApiResponse.onSuccess(storeService.reportStore(report));
    }

}
