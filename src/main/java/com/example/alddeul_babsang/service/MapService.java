package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MapService {

    private final StoreRepository storeRepository;

    // 지도용 착한 업소 리스트 조회
    public List<StoreDTO.MapStore> getStoreList() {
        List<Store> stores = storeRepository.findAll();
        // 예비밥상 조회

        return stores.stream()
                .map(StoreConverter::toMapStore)
                .collect(Collectors.toList());
    }

    // 특정 착한 업소 조회
    public StoreDTO.StoreInfo getStore(int storeId) {
        // store id 조회 -> 예외 처리
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        return StoreConverter.toStoreInfo(store);
    }
}
