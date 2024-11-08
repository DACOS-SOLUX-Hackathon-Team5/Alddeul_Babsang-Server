package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(long userId);

    Optional<Favorite> findByUserIdAndStoreId(long userId, long storeId);

}