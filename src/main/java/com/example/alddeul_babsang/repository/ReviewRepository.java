package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.star_rating) FROM Review r WHERE r.store.id = :storeId")
    Float findAverageRatingByStoreId(@Param("storeId") Long storeId);
}
