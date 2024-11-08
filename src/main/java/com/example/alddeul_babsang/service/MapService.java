package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.MapConverter;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Menu;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.MenuRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MapService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    // 착한 업소 리스트 조회
    public List<StoreDTO.MapStore> getMapStoreList() {

        // 예비밥상
        // 반영 기능
        // 넣기

        // 착한 업소만 조회
        List<Store> stores = storeRepository.findAllByStatus(Status.GOOD);

        return stores.stream()
                .map(StoreConverter::toMapStore)
                .collect(Collectors.toList());
    }

    // 착한 업소 조회
    public StoreDTO.StoreInfo getStore(Long storeId, Long userId) {
        System.out.println("서비스 단의 userId: " + userId);
        // store id 조회 -> 예외 처리
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        userRepository.findById(userId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.USER_ERROR_ID));

        // Favorite 존재 여부 확인
        boolean isFavorite = favoriteRepository.existsByUserIdAndStoreId(userId, storeId);
        System.out.println("서비스 단의 isFavorite 여부: " + isFavorite);
        return StoreConverter.toStoreInfo(store, isFavorite);
    }
}
