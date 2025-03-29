package com.example.alddeul_babsang.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeoRequestDto {
	private double latitude;
	private double longitude;
	private int clusterId;
	private Long storeRealId;
}
