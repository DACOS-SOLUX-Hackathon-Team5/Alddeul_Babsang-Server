package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.CoordinatesService;
import com.example.alddeul_babsang.service.CsvImportService;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import com.opencsv.exceptions.CsvValidationException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController("/temp")
@RequiredArgsConstructor
public class TempComtroller {

    private final CoordinatesService coordinatesService;
    private final CsvImportService csvImportService;

    // 업소 좌표 조회
    @GetMapping("/testCoordinates")
    @Operation(summary = "주소 -> 좌표", description = "도로명 주소 제한.")
    public ApiResponse<StoreDTO.Coordinates> getCoordinates(@RequestParam String address) {
        System.out.println(address);
        return ApiResponse.onSuccess(coordinatesService.getStoreCoordinates(address));
    }

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "csv -> db 올리기", description = "업소 db 저장.")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            File csvFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(csvFile); // 임시 파일로 저장
            csvImportService.importDataFromCsv(csvFile.getAbsolutePath());
            return "CSV file imported successfully.";
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return "Failed to import CSV file.";
        }
    }
}
