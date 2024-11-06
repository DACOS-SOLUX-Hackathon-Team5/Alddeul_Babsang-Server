package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Menu;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.StoreRepository;
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
        Store store = storeRepository.findById(storeId).orElse(null);
        Menu menu = store.getMenu();

        return StoreConverter.toStoreDetail(store, menu);
    }
}
