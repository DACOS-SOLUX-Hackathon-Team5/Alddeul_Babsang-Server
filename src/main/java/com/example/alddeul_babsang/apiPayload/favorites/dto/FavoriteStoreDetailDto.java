package com.example.alddeul_babsang.apiPayload.favorites.dto;

import com.example.alddeul_babsang.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class FavoriteStoreDetailDto {
    private int restaurantId;
    private String name;
    private Category category;
    private String address;
    private String contact;
    private String restaurantImageUrl;
    private boolean favorite;
}