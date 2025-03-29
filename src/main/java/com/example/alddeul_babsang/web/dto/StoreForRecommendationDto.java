package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class StoreForRecommendationDto {
	private String name;
	private String region;
	private String category;
	private String top5Tags;
}
