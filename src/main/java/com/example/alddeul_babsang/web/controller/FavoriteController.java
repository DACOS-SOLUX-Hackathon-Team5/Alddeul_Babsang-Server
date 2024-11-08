package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.FavoriteService;
import com.example.alddeul_babsang.web.dto.FavoriteResponseDto;
import com.example.alddeul_babsang.web.dto.FavoriteRequestDto;


import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ApiResponse<FavoriteResponseDto> getFavoritesByUser(@PathVariable long userId) {
        FavoriteResponseDto favoritesByUser = favoriteService.getFavoritesByUser(userId);
        return ApiResponse.onSuccess(favoritesByUser);
    }

    @PostMapping
    public ApiResponse<Map<String, String>> changeFavoriteStore(@Valid @RequestBody FavoriteRequestDto request) {
        long newFavoriteStoreId = request.getStoreId();
        long userId = request.getUserId();
        String message = favoriteService.changeFavoriteStore(userId, newFavoriteStoreId);

        Map<String, String> response = new HashMap<>();
        response.put("message", message); // 콜론 제거

        return ApiResponse.onSuccess(response);
    }
}
