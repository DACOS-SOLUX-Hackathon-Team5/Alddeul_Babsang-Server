package com.example.alddeul_babsang.web.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRequestDto {
    @NotNull(message = "User ID는 필수입니다.")
    private int userId;
    @NotNull(message = "Store ID는 필수입니다.")
    private int storeId;

}
