package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private  final FavoriteRepository favoriteRepository;
    private  final StoreRepository storeRepository;

    public List<RecommendationResponseDto> getRecommendNearestStore(Long storeId) {
        // Store 가져오기
        Store currentStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        // 결과 리스트 선언
        List<RecommendationResponseDto> response = new ArrayList<>();

        // Store의 Status 확인
        if (currentStore.getStatus() == Status.PREGOOD) {
            // Status가 PREGOOD이면 빈 리스트 반환
            return response;
        }

        // Status가 GOOD일 경우 진행
        int clusterId = (currentStore.getCluster2() != null) ? currentStore.getCluster2() : 1; // null이면 1로 설정
        double latitude = currentStore.getLatitude();
        double longitude = currentStore.getLongitude();
        System.out.println(clusterId + "+++++++++++++++++++");
        System.out.println(latitude);
        System.out.println(longitude);

        // 추천 결과 저장할 리스트 선언
        List<String> recommendations = new ArrayList<>();

        String[] command = {
                "python",
                "src/main/resources/from_geopy.py",  // Python 스크립트 경로
                String.valueOf(latitude), // 위도
                String.valueOf(longitude), // 경도
                String.valueOf(clusterId), // 클러스터 ID
                String.valueOf(storeId)
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                recommendations.add(line); // Python 스크립트의 출력 수집
            }
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing Python script", e);
        }

        System.out.println(recommendations);
        List<Store> recommendStores = storeRepository.findByNameIn(recommendations);
        response = recommendStores.stream()
                .map(store -> new RecommendationResponseDto(
                        store.getName(),
                        store.getCategory(),
                        store.getRegion(),
                        store.getId()
                ))
                .collect(Collectors.toList());
        return response;
    }

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
                        store.getRegion(),
                        store.getId()
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

