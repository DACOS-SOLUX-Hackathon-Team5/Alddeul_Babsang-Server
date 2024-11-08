
package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.apiPayload.ApiResponse;
import com.example.alddeul_babsang.service.RecommendService;
import com.example.alddeul_babsang.web.dto.FavoriteResponseDto;
import com.example.alddeul_babsang.web.dto.FavoriteRequestDto;
import com.example.alddeul_babsang.service.CoordinatesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    //유저아이디로 좋아요리스트 확인
    //좋아요 등록하기
    //좋아요 삭제하기
    @Autowired
    private CoordinatesService.FavoriteService favoriteService;
    private final RecommendService recommendService;

    @GetMapping("/{userId}")
    public ApiResponse<FavoriteResponseDto> getFavoritesByUser(@PathVariable int userId) {
        System.out.println(recommendService.getRecommendByUser(userId));
        FavoriteResponseDto favoritesByUser = favoriteService.getFavoritesByUser(userId);
        return ApiResponse.onSuccess(favoritesByUser);
    }

    @PostMapping
    public ApiResponse<Map<String, String>> changeFavoriteStore(@Valid @RequestBody FavoriteRequestDto request){
        int newFavoriteStoreId = request.getStoreId();
        int userId=request.getUserId();
        String message=favoriteService.changeFavoriteStore(userId,newFavoriteStoreId);
        Map<String, String> response = new HashMap<>();
        response.put("message: ", message);
        return ApiResponse.onSuccess(response);
    }

}
