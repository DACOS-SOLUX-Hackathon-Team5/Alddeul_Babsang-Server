package com.example.alddeul_babsang.entity.enums;

public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    WESTERN_JAPANESE("경양식/일식"),
    OTHER("기타외식업");

    private final String koreanName;

    Category(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }}
