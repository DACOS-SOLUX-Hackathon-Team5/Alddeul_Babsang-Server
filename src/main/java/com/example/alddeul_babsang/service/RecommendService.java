package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.GeoRequestDto;
import com.example.alddeul_babsang.web.dto.RecommendationResponseDto;
import com.example.alddeul_babsang.web.dto.StoreForRecommendationDto;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Collections;
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
        List<Integer> result = new ArrayList<>();

        System.out.println(currentStore.getName());
        // Store의 Status 확인
        if (currentStore.getStatus() == Status.PREGOOD) {
            // Status가 PREGOOD이면 완전 빈 불변 리스트 반환
            return Collections.emptyList();
        }

        GeoRequestDto geoDto = new GeoRequestDto(
            currentStore.getLatitude(),
            currentStore.getLongitude(),
            currentStore.getCluster2() != null ? currentStore.getCluster2() : 1,
            currentStore.getRealId()
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(geoDto);
            System.out.println(jsonData);

            ProcessBuilder builder = new ProcessBuilder("python", "src/main/resources/from_geopy.py");
            Process process = builder.start();

            // JSON 전송
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(jsonData);
                writer.flush();
            }

            // 결과 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if (line.startsWith("[")) {
                        result = mapper.readValue(line, new TypeReference<List<Integer>>() {});
                    }
                }
            }

            process.waitFor();

        } catch (Exception e) {
            throw new RuntimeException("파이썬 추천 실행 중 오류 발생", e);
        }


        // 6. DB에서 realId 기준으로 Store 찾기
        List<Store> stores = storeRepository.findByRealIdIn(result);

        // 7. DTO로 변환해서 반환
        return stores.stream()
            .map(store -> new RecommendationResponseDto(
                store.getName(),
                store.getCategory(),
                store.getRegion(),
                store.getId()
            ))
            .collect(Collectors.toList());
    }


    public List<RecommendationResponseDto> getRecommendByUser(Long userId) {

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        if (favorites.isEmpty())
            return new ArrayList<>(); //좋아요 누른 거 없으면 빈리스트

        List<StoreForRecommendationDto> storeDtos = convertFavoritesToDtos(favorites);
        String jsonData = convertToJson(storeDtos);
        List<String> recommendedStoreIds = callPythonAndParseResult(jsonData);

        return convertStoreIdsToResponse(recommendedStoreIds);
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
    private List<RecommendationResponseDto> convertStoreIdsToResponse(List<String> storeIds) {
        List<RecommendationResponseDto> result = new ArrayList<>();

        for (String realIdStr : storeIds) {
            try {
                int realId = Integer.parseInt(realIdStr); // 파이썬에서 받은 id는 문자열이므로 정수로 변환
                Store store = storeRepository.findByRealId(realId);
                if (store != null) {
                    result.add(new RecommendationResponseDto(
                        store.getName(),
                        store.getCategory(),
                        store.getRegion(),
                        store.getId()
                    ));
                }
            } catch (NumberFormatException e) {
                System.err.println("잘못된 ID 형식: " + realIdStr);
            }
        }

        return result;
    }







}

