package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ReviewResponseDto {
    private int storeId;
    private int userId;
    private String message;
}
