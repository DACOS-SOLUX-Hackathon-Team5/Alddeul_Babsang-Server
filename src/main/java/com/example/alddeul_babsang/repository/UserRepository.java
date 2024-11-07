package com.example.alddeul_babsang.repository;

import com.example.alddeul_babsang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
