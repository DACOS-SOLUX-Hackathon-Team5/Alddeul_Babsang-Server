package com.example.alddeul_babsang.favorites.repository;

import com.example.alddeul_babsang.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Integer> {
    Optional<Store> findById(int newFavoriteStoreId);
}
