package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Menu;
import com.example.alddeul_babsang.entity.Review;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.ReviewDTO;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreService {

    private StoreRepository storeRepository;

    // 업소 리스트 조회
    public List<StoreDTO.StoreInfo> getStoreList(Status status) {
        // 업소 status에 따라 처리
        List<Store> stores = storeRepository.findAllByStatus(status);

        return stores.stream()
                .map(StoreConverter::toStoreInfo)
                .collect(Collectors.toList());
    }

    // 업소 상세 조회
    public StoreDTO.StoreDetail getStoreInfoDetail(Long storeId) {
        // store id 조회 -> 예외 처리
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        // store 메뉴 조회
        Menu menu = store.getMenu();
        return StoreConverter.toStoreDetail(store, menu);
    }

    // 업소 리뷰 조회
    public ReviewDTO.StoreReviews getReviewList(Long storeId) {
        // store id 조회 -> 예외 처리
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        // store 리뷰 조회
        List<Review> reviews = store.getReviewList();
        return StoreConverter.toStoreReviews(reviews);
    }
}
