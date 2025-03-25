package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.GeneralException;
import com.example.alddeul_babsang.entity.Review;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.repository.ReviewRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final S3Service s3Service;
    private  final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    public ReviewResponseDto createReview(int storeId, int userId, float rating, String content, MultipartFile reviewImage) {
        User user=userRepository.findById(userId).orElseThrow(() ->  new GeneralException(ErrorStatus._BAD_REQUEST));
        Store store= storeRepository.findById(storeId).orElseThrow(() ->  new GeneralException(ErrorStatus._BAD_REQUEST));
        String imagePath = null;
        if (reviewImage != null && !reviewImage.isEmpty()) {
            try {
                imagePath = s3Service.uploadFile(reviewImage, "reviews");
            } catch (GeneralException e) {
                throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Review review = new Review(content, rating, imagePath, user, store);
        // 리뷰 저장
        reviewRepository.save(review);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(userId,storeId, "리뷰등록성공");
        return reviewResponseDto;
    }
}

