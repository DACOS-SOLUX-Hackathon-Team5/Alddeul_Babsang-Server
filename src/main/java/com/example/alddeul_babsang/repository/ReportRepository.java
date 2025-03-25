package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.Report;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByUserAndStore(User user, Store store);
}
