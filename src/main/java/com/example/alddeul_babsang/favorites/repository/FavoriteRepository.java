package com.example.alddeul_babsang.favorites.repository;

import com.example.alddeul_babsang.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUserId(int userId);

    Optional<Favorite> findByUserIdAndStoreId(int userId, int storeId);
}