package com.example.alddeul_babsang.converter;

import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import lombok.Builder;
import lombok.Getter;

public class MapConverter {
    // 위도-경도가 있는 업소 정보 형식으로 매핑
    public static StoreDTO.Coordinates toCoordinates(String latitude, String longitude) {
        return StoreDTO.Coordinates.builder()
                .latitude(latitude).longitude(longitude).build();
    }

}
