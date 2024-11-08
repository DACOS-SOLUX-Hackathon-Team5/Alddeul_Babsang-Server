package com.example.alddeul_babsang.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private float star_rating;

    private String image;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;



    public Review(String content, float rating, String imagePath, User user, Store store) {
        this.content = content;
        this.star_rating = rating;
        this.image = imagePath;
        this.user = user;
        this.store = store;
    }
}
