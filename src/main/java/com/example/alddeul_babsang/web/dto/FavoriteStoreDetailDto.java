package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class FavoriteStoreDetailDto {
    private Long restaurantId;
    private String name;
    //private Category category;
    private String address;
    private String contact;
    private String restaurantImageUrl;
    private boolean favorite;
}
