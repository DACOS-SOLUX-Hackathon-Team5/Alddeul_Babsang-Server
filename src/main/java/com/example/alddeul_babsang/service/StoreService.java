package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.GeneralException;
import com.example.alddeul_babsang.apiPayload.exception.handler.TempHandler;
import com.example.alddeul_babsang.converter.StoreConverter;
import com.example.alddeul_babsang.entity.*;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.*;
import com.example.alddeul_babsang.web.dto.ReviewDTO;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final FavoriteRepository favoriteRepository;
    private final CoordinatesService coordinatesService;
    private final S3Service s3Service;

    // 업소 리스트 조회
    public List<StoreDTO.StoreInfo> getStoreList(Status status, Long userId) {
        System.out.println("서비스 단의 userId 여부: " + userId);

        // 업소 status에 따라 처리
        List<Store> stores = storeRepository.findAllByStatus(status);

        // User 존재 여부 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.USER_ERROR_ID));

        // 각 Store가 즐겨찾기인지 확인하여 리스트 반환
        return stores.stream()
                .map(store -> {
                    boolean isFavorite = favoriteRepository.existsByUserIdAndStoreId(userId, store.getId());
                    System.out.println("서비스 단의 isFavorite 여부: " + isFavorite);

                    return StoreConverter.toStoreInfo(store, isFavorite);
                })
                .collect(Collectors.toList());
    }

    // 업소 상세 조회
    public StoreDTO.StoreDetail getStoreInfoDetail(Long storeId, Long userId) {
        // store id 조회 -> 예외 처리
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.STORE_ERROR_ID));

        userRepository.findById(userId)
                 .orElseThrow(() -> new TempHandler(ErrorStatus.USER_ERROR_ID));

        // Favorite 존재 여부 확인
        boolean isFavorite = favoriteRepository.existsByUserIdAndStoreId(userId, storeId);

         // store 메뉴 조회
        Menu menu = store.getMenu();
        return StoreConverter.toStoreDetail(store, menu, isFavorite);
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

        String imagePath = null;
        MultipartFile reviewImage = report.getImageUrl();
        if (reviewImage != null && !reviewImage.isEmpty()) {
            try {
                imagePath = s3Service.uploadFile(reviewImage, "reviews");
            } catch (GeneralException e) {
                throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        // 주소 -> 위도, 경도 변환
        StoreDTO.Coordinates coordinates = coordinatesService.getStoreCoordinates(report.getAddress());
        double latitude = Double.parseDouble(coordinates.getLatitude());
        double longitude = Double.parseDouble(coordinates.getLongitude());

        // 업소 저장
        Store store = StoreConverter.toStoreEntity(report, latitude, longitude, imagePath);
        Menu menu = StoreConverter.toMenuEntity(report);
        saveReport(user, store, menu); // Report 엔티티에 유저와 가게 관계 저장

        System.out.println(imagePath);
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
