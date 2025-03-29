package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.apiPayload.code.status.ErrorStatus;
import com.example.alddeul_babsang.apiPayload.exception.GeneralException;
import com.example.alddeul_babsang.entity.Review;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.repository.ReviewRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.ReviewRequestDto;
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

    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        int userId = reviewRequestDto.getUserId();
        int storeId = reviewRequestDto.getStoreId();

        User user=userRepository.findById(userId).orElseThrow(() ->  new GeneralException(ErrorStatus._BAD_REQUEST));
        Store store= storeRepository.findById(storeId).orElseThrow(() ->  new GeneralException(ErrorStatus._BAD_REQUEST));
        String imagePath = uploadReviewImage(reviewRequestDto.getImage());
        Review review = new Review(reviewRequestDto.getContent(), reviewRequestDto.getRating(), imagePath, user, store);

        reviewRepository.save(review);
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(userId,storeId, "리뷰등록성공");
        return reviewResponseDto;
    }


    private String uploadReviewImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        try {
            return s3Service.uploadFile(image, "reviews");
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }

}

