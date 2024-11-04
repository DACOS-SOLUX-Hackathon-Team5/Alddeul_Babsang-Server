package com.example.alddeul_babsang.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name1;
    private int price1;

    private String name2;
    private int price2;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
