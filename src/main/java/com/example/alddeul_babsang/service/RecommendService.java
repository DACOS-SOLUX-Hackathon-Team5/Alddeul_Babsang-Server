package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.RecommendationResponseDto;
import com.example.alddeul_babsang.web.dto.StoreForRecommendationDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;

    public List<RecommendationResponseDto> getRecommendNearestStore(Long storeId) {
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

            //결과값 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                recommendations.add(line); // Python 스크립트의 출력 수집
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("[Python 에러] " + errorLine);
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

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        if (favorites.isEmpty())
            return new ArrayList<>(); //좋아요 누른 거 없으면 빈리스트

        List<StoreForRecommendationDto> storeDtos = convertFavoritesToDtos(favorites);
        String jsonData = convertToJson(storeDtos);
        List<String> recommendedStoreNames = callPythonAndParseResult(jsonData);

        return convertStoreNamesToResponse(recommendedStoreNames);
    }

    //좋아요 누른 것둘을 추천을 위한 dto리스트로 만드는 메소드
    private List<StoreForRecommendationDto> convertFavoritesToDtos(List<Favorite> favorites) {
        return favorites.stream()
            .map(fav -> {
                Store store = fav.getStore();
                return new StoreForRecommendationDto(
                    store.getName(),
                    store.getRegion(),
                    store.getCategory().getKoreanName(),
                    store.getTop5Tags()
                );
            })
            .collect(Collectors.toList());
    }
    //파이썬에 json으로 값을 전달하기 위해, json 변환 메소드
    private String convertToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data);
        } catch (IOException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }
    }

    //파이썬을 실행하는 메소드
    private List<String> callPythonAndParseResult(String jsonData) {
        List<String> recommendations = new ArrayList<>();
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "src/main/resources/recommend.py");
            Process process = pb.start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(jsonData);
                writer.flush();
                writer.close();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("recommended:")) {
                        recommendations = parseRecommendationLine(line);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("파이썬 추천 실행 실패", e);
        }

        return recommendations;
    }
    //파이썬에서 온 추천 문자열 파싱 메소드
    private List<String> parseRecommendationLine(String line) {
        List<String> result = new ArrayList<>();
        String listString = line.split("recommended:")[1].trim();
        listString = listString.substring(1, listString.length() - 1);
        String[] items = listString.split(",");

        for (String item : items) {
            String cleaned = item.trim().replaceAll("['\"]", "");
            result.add(cleaned);
        }
        return result;
    }
    private List<RecommendationResponseDto> convertStoreNamesToResponse(List<String> storeNames) {
        List<RecommendationResponseDto> result = new ArrayList<>();
        for (String name : storeNames) {
            //중복인 경우를 고려, 리스트로 찾기
            List<Store> stores = storeRepository.findAllByName(name);
            if (!stores.isEmpty()) {
                Store store = stores.get(0); //첫번째만 사용하기
                result.add(new RecommendationResponseDto(
                    store.getName(),
                    store.getCategory(),
                    store.getRegion(),
                    store.getId()
                ));
            }
        }
        return result;
    }






}

