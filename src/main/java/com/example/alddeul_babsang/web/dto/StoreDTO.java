package com.example.alddeul_babsang.web.dto;

import com.example.alddeul_babsang.entity.enums.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


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
        private final Long storeId; // 업소 아이디
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

    // 제보 업소 등록 요청 정보
    @Getter
    @Builder
    public static class StoreReport {
        private final String name;       // 업소 이름
        private final Category category; // 업종
        private final String address;    // 주소
        private final String contact;    // 연락처
        private final String menuName1;  // 대표메뉴1
        private final Integer menuPrice1;
        private final String menuName2;  // 대표메뉴2
        private final Integer menuPrice2;
        private final MultipartFile imageUrl;   // 이미지 주소
    }

    // 좌표변환 테스트
    @Getter
    @Builder
    public static class CoordinatesRequest {
        private String service; //
        private String query;  //
        private String select;
        private String crs;
    }

    // 좌표변환 테스트
    @Getter
    @Builder
    public static class Coordinates {
        private String longitude; // 경도
        private String latitude;  // 위도
    }

    // 업소 CSV 데이터
    @Getter
    @Builder
    public static class StoreCsvData {
        private Long storeId;            // 업소아이디
        private String storeName;         // 업소명
        private Integer categoryCode;     // 분류코드
        private String address;           // 업소 주소
        private String storeContact;       // 업소 전화번호
        private String region;            // 구
        private double latitude;          // latitude
        private double longitude;         // longitude
        private String top5Tags;          // Top 5 Tags and Counts
        private Integer cluster1;         // cluster
        private Integer cluster2;         // cluster2
        private Integer cluster3;         // cluster3
        private String menu1;             // 메뉴1
        private String menu2;             // 메뉴2
    }
}
