package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.config.VWorldConfig;
import com.example.alddeul_babsang.converter.MapConverter;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
@AllArgsConstructor
public class CoordinatesService {

    private final VWorldConfig config; // VWorldConfig 인스턴스 주입
    private final Map<String, String> defaultParams;
    private final RestTemplate restTemplate;

    public StoreDTO.Coordinates getStoreCoordinates(String address) {
        String apiUrl = config.getApiUrl();

//        // defaultParams의 내용을 출력하여 올바르게 주입되었는지 확인
//        System.out.println("defaultParams: " + defaultParams);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("address", address);

        // defaultParams의 각 파라미터를 URI에 추가
        defaultParams.forEach(uriBuilder::queryParam);

        URI uri = uriBuilder.encode().build().toUri();

        System.out.println("Final URI: " + uri);

        String response = restTemplate.getForObject(uri, String.class);

//        System.out.println("API Response: " + response);
        try {
            String status = JsonPath.parse(response).read("$.response.status");
            if (!"OK".equals(status)) {
                throw new IllegalArgumentException("API 요청 실패: 상태 = " + status);
            }

            String latitude = JsonPath.parse(response).read("$.response.result.point.y");
            String longitude = JsonPath.parse(response).read("$.response.result.point.x");

            return MapConverter.toCoordinates(latitude, longitude);

        } catch (PathNotFoundException e) {
            throw new IllegalArgumentException("좌표 변환 실패: 응답에 결과 데이터가 없습니다.");
        }
    }
}
