package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.RecommendationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private  final FavoriteRepository favoriteRepository;
    private  final StoreRepository storeRepository;


    public List<RecommendationResponseDto> getRecommendByUser(Long userId) {
        List<String> recommendations = new ArrayList<>();
        List<RecommendationResponseDto> response = new ArrayList<>();
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        if (favorites.isEmpty()){
            return response;
        }
        else {
            Favorite favorite=getRandomFavorite(favorites);
            Store store = favorite.getStore();
            String region = store.getRegion();
            String category = store.getCategory().getKoreanName();
            String tags = store.getTop5Tags();
            System.out.println(category+store+region+tags);

            try {

                ProcessBuilder processBuilder = new ProcessBuilder(
                        "python","src/main/resources/recommend.py",
                        category,
                        tags,
                        region
                );
                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        recommendations.add(line);  // Add each line to recommendations list
                    }
                }
                process.waitFor();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Failed to execute recommendation script", e);
            }
        }
        System.out.println(recommendations);
        List<Store> recommendStores = storeRepository.findByNameIn(recommendations);
        response = recommendStores.stream()
                .map(store -> new RecommendationResponseDto(
                        store.getName(),
                        store.getCategory(),
                        store.getRegion()
                ))
                .collect(Collectors.toList());

        return  response;
}
        private Favorite getRandomFavorite(List<Favorite> favorites){
        Random random = new Random();
        int randomIndex = random.nextInt(favorites.size());
        return favorites.get(randomIndex);
    }



    }

