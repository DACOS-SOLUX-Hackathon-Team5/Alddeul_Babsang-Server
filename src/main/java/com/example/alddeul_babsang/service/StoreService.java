package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.Menu;
import com.example.alddeul_babsang.entity.Review;
import com.example.alddeul_babsang.entity.Report;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.MenuRepository;
import com.example.alddeul_babsang.repository.ReportRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.web.dto.ReviewDTO;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReportRepository reportRepository;
    private final MenuRepository menuRepository;

    private final CoordinatesService coordinatesService;

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

    // 업소 제보 등록
    public String reportStore(StoreDTO.StoreReport report) {

        // User ID 조회 -> 수정할 것
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new TempHandler(ErrorStatus.USER_ERROR_ID));

        // 유저가 이미 제보했는지 확인
        if (isAlreadyReported(user, report.getName())) {
            return "이미 제보한 업소입니다.";
        }

        // 주소 -> 위도, 경도 변환
        StoreDTO.Coordinates coordinates = coordinatesService.getStoreCoordinates(report.getAddress());
        double latitude = Double.parseDouble(coordinates.getLatitude());
        double longitude = Double.parseDouble(coordinates.getLongitude());

        // 주소 -> 구 추출

//        System.out.println("latitude: " + latitude + "longitude: "+longitude);

        // 업소 저장
        Store store = StoreConverter.toStoreEntity(report, latitude, longitude);
        Menu menu = StoreConverter.toMenuEntity(report);
        saveReport(user, store, menu); // Report 엔티티에 유저와 가게 관계 저장

        return "제보 완료";
    }

    // 업소 이름, 주소 중복 확인
    private boolean isAlreadyReported(User user, String storeName) {
        Optional<Store> store = storeRepository.findByName(storeName);
        return store.isPresent() && reportRepository.existsByUserAndStore(user, store.get());
    }


    private void saveReport(User user, Store store, Menu menu) {
        Report report = new Report();
        report.setUser(user);
        report.setStore(store);

        storeRepository.save(store);
        menuRepository.save(menu);
        reportRepository.save(report);
    }
}
