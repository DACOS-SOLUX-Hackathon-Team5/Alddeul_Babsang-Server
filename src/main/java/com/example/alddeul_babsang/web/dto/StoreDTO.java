package com.example.alddeul_babsang.web.dto;

import com.example.alddeul_babsang.entity.enums.Category;
import lombok.Builder;
import lombok.Getter;


public class StoreDTO {
    // 위도-경도가 있는 업소 정보
    @Getter
    @Builder
    public static class MapStore {
        private final Long storeId; // 업소 아이디
        private final String name;  // 업소 이름
        private final Category category; // 업종
        private final String address;    // 주소
        private final String region;     // 구
        private final double latitude;   // 위도
        private final double longitude;  // 경도
    }

    // 업소 정보
    @Getter
    @Builder
    public static class StoreInfo {
        private final String name;       // 업소 이름
        private final Category category; // 업종
        private final String address;    // 주소
        private final String contact;    // 연락처
        private final String imageUrl;   // 이미지 주소
        private final boolean isFavorite;    // 좋아요 여부
    }

    // 업소 상세 정보
    @Getter
    @Builder
    public static class StoreDetail {
        private final StoreInfo storeInfo;
        private final MenuInfo menu1; // 대표메뉴 1
        private final MenuInfo menu2; // 대표메뉴 2
    }

    // 메뉴 정보
    @Getter
    @Builder
    public static class MenuInfo {
        private final String name;   // 이름
        private final Integer price; // 가격
    }
}
