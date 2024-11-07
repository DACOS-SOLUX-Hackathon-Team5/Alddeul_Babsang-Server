package com.example.alddeul_babsang.favorites.repository;
import com.example.alddeul_babsang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
   Optional<User> findById(int userID);
}
