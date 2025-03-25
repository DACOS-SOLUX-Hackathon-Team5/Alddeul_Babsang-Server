package com.example.alddeul_babsang.entity;

import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.entity.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long realId;

    private String name;

    private String address;

    private String region;

    private double latitude;

    private double longitude;

    private String contact;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String thumnail;

    private float averageRating;

    @CreatedDate
    private LocalDateTime createdAt;

    // 추가된 필드들
    private String top5Tags;
    private Integer cluster1;
    private Integer cluster2;
    private String menu1;
    private String menu2;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Favorite> favoriteList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private Menu menu;
}

