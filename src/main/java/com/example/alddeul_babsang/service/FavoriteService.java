package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.repository.FavoriteRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.example.alddeul_babsang.repository.UserRepository;
import com.example.alddeul_babsang.web.dto.FavoriteResponseDto;
import com.example.alddeul_babsang.web.dto.FavoriteStoreDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 주입 방식으로 변경, @AllArgsConstructor 삭제
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 user 찾을 수 없음. ID: " + id));
    }

    public Optional<Favorite> checkIfFavoriteExists(User user, Store store) {
        return favoriteRepository.findByUserIdAndStoreId(user.getId(), store.getId());
    }

    public FavoriteResponseDto getFavoritesByUser(long userId) {
        User user = findUserById(userId);
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
        return new FavoriteResponseDto(favoriteStoresDetails);
    }

    public String changeFavoriteStore(long userId, long newFavoriteStoreId) {
        User user = findUserById(userId);
        Store store = storeRepository.findById(newFavoriteStoreId)
                .orElseThrow(() -> new IllegalArgumentException("해당 storeId 찾을 수 없음. ID: " + newFavoriteStoreId));

        Optional<Favorite> favoriteStoreToChange = checkIfFavoriteExists(user, store);
        if (favoriteStoreToChange.isPresent()) {
            favoriteRepository.delete(favoriteStoreToChange.get());
            return "좋아요 취소";
        } else {
            Favorite favorite = new Favorite(user, store);
            favoriteRepository.save(favorite);
            return "좋아요 완료";
        }
    }
}
