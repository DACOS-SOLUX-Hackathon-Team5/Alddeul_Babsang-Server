package com.example.alddeul_babsang.apiPayload.review.repository;

import com.example.alddeul_babsang.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

}
