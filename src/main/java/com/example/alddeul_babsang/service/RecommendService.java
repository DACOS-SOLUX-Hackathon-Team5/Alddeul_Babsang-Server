package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.FavoriteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private  final FavoriteRepository favoriteRepository;
    private  final StoreRepository storeRepository;

    public List<String> getRecommendByUser(int userId) {
        List<String> recommendations = new ArrayList<>();
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        if (favorites.isEmpty()){
            return recommendations;
        }
        else {
            Random random = new Random();
            int randomIndex = random.nextInt(favorites.size());
            Favorite favorite = favorites.get(randomIndex);
            Store store = favorite.getStore();
            String region = store.getRegion();
            String name = store.getName();
            String category = store.getCategory();
            String tags = "[[재료가 신선해요], [친절해요], [가성비가 좋아요], [매장이 넓어요]]";
            try {
                // ProcessBuilder로 Python 스크립트 실행 설정
                ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\Users\\82107\\Desktop\\Alddeul_Babsang-Server\\src\\main\\resources\\recommend.py", category, tags, region);
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while (true) {
                    try {
                        if (!((line = reader.readLine()) != null)) break;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    recommendations.add(line);  // 추천 가게 이름을 리스트에 추가
                }
                try {
                    process.waitFor();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                // 추천 가게 목록을 반환
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recommendations;
        }
    }
}
