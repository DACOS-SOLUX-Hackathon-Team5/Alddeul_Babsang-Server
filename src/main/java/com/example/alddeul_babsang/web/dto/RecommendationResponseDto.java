package com.example.alddeul_babsang.web.dto;

import com.example.alddeul_babsang.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendationResponseDto {
    private  String name;       // 업소 이름
    private  Category category; // 업종
    private String region;
    private Long storeId;
}
