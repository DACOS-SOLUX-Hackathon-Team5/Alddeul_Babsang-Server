package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.web.dto.FavoriteStoreDetailDto;
import com.example.alddeul_babsang.config.VWorldConfig;
import com.example.alddeul_babsang.converter.MapConverter;
import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.StoreDTO;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Service
    @RequiredArgsConstructor
    public static class FavoriteService {

        @Autowired
        private final FavoriteRepository favoriteRepository;
        private final UserRepository userRepository;
        private final StoreRepository storeRepository;
        User findUserById(long id)
        {     User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 user 찾을 수 없음. ID: " + id));
            return user;
        }
        public Optional<Favorite> checkIfFavoriteExists(User user, Store store) {
            Optional<Favorite> favorite=favoriteRepository.findByUserIdAndStoreId(user.getId(), store.getId());
            return favorite;
        }


        public List<FavoriteStoreDetailDto> getFavoritesByUser(long userId) {
            User user=findUserById(userId);
            List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
            List<FavoriteStoreDetailDto> favoriteStoresDetails = favorites.stream()
                    .map(favorite -> new FavoriteStoreDetailDto(
                            favorite.getStore().getId(),
                            favorite.getStore().getName(),
                            favorite.getStore().getCategory(),
                            favorite.getStore().getAddress(),
                            favorite.getStore().getContact(),
                            favorite.getStore().getThumnail(),
                            true
                    ))
                    .collect(Collectors.toList());
            return  favoriteStoresDetails;
        }


        public String changeFavoriteStore(int userId, int newFavoriteStoreId) {
            User user=findUserById(userId);
            Store store= storeRepository.findById(newFavoriteStoreId).orElseThrow(() -> new IllegalArgumentException("해당 storeId 찾을 수 없음. ID: " + newFavoriteStoreId));
            Optional<Favorite> favoriteStoreToChange = checkIfFavoriteExists(user,store);
            if(favoriteStoreToChange.isPresent()){
                favoriteRepository.delete(favoriteStoreToChange.get());
                return "좋아요 취소";
            }
            else{
                Favorite favorite=new Favorite(user,store);
                favoriteRepository.save(favorite);
                return "좋아요 완료";
            }

        }

    }
}
