package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.CoordinatesService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
@RequiredArgsConstructor
public class TempComtroller {

    private final CoordinatesService coordinatesService;

    @GetMapping("/health-chcek")
    @Operation(summary = "서버 헬스 체크 api", description = "서버 헬스 체크용 입니다.")
    public ApiResponse<String> getHealthCheck() {
        return ApiResponse.onSuccess("health check ok");
    }

    // 업소 좌표 조회
    @GetMapping("/testCoordinates")
    @Operation(summary = "주소 -> 좌표", description = "도로명 주소 제한.")
    public ApiResponse<StoreDTO.Coordinates> getCoordinates(@RequestParam String address) {
        System.out.println(address);
        return ApiResponse.onSuccess(coordinatesService.getStoreCoordinates(address));
    }
}
