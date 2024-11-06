package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStatus(Status status);
}
