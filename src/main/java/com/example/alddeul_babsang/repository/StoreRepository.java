package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findById(long newFavoriteStoreId);
    Optional<Store> findByName(String name);
    List<Store> findAllByStatus(Status status);

}
