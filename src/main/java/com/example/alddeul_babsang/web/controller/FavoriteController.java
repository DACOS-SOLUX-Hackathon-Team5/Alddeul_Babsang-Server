
package com.example.alddeul_babsang.web.controller;

import com.example.alddeul_babsang.web.dto.FavoriteStoreDetailDto;
import com.example.alddeul_babsang.web.dto.FavoriteRequestDto;
import com.example.alddeul_babsang.service.CoordinatesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    //유저아이디로 좋아요리스트 확인
    //좋아요 등록하기
    //좋아요 삭제하기
    @Autowired
    private CoordinatesService.FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFavoritesByUser(@PathVariable int userId) {
        List<FavoriteStoreDetailDto> favoriteStores= favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.ok().body(Map.of("favoriteRestaurants", favoriteStores));
    }

    @PostMapping
    public ResponseEntity<?> changeFavoriteStore(@Valid @RequestBody FavoriteRequestDto request){
        int newFavoriteStoreId = request.getStoreId();
        int userId=request.getUserId();
        String message=favoriteService.changeFavoriteStore(userId,newFavoriteStoreId);
        Map<String, String> response = new HashMap<>();
        response.put("message: ", message);
        return ResponseEntity.ok().body(response);
    }




}
