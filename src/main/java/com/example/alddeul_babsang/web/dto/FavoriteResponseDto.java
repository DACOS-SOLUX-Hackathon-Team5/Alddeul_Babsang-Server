package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FavoriteResponseDto {
    private List<FavoriteStoreDetailDto> favoriteStoreDetailDtos;

}

