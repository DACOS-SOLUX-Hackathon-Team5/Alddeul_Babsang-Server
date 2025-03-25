package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@Data
public class ReviewRequestDto {
    private int storeId;
    private int userId;
    private float rating;
    private String content;
    private MultipartFile image;




}
