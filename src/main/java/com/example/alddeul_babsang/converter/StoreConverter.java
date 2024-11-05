package com.example.alddeul_babsang.converter;

import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.web.dto.StoreDTO;

public class StoreConverter {

    // 위도-경도가 있는 업소 정보 형식으로 매칭
    public static StoreDTO.MapStore toMapStore(Store store) {
        return StoreDTO.MapStore.builder()
                .storeId(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .address(store.getAddress())
                .region(extractRegion(store.getAddress()))
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .build();
    }

    // 주소에서 지역 구 추출
    public static String extractRegion(String address) {
        int startIndex = address.indexOf(" ") + 1; // 첫 번째 공백 이후 시작 (서울시 제외)
        int endIndex = address.indexOf("구") + 1;  // '구'까지 포함

        // 주소에 지역 구가 존재하면 substring
        if (startIndex > 0 && endIndex > 0 && endIndex > startIndex) {
            return address.substring(startIndex, endIndex);
        }

        return "알 수 없음"; // 잘못된 주소 경로일 경우
    }

    // 업소 정보 형식으로 매칭
    public static StoreDTO.StoreInfo toStoreInfo(Store store) {
        return StoreDTO.StoreInfo.builder()
                .name(store.getName())
                .category(store.getCategory())
                .address(store.getAddress())
                .contact(store.getContact())
                .imageUrl(store.getThumnail())
                // 유저 id에 따라 좋아요 기능 반영해야 함
                // .isFavorite()
                .build();
    }
}
