package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendationResponseDto {
    private  String name;       // 업소 이름
    private String region;
    private Long storeId;
}
