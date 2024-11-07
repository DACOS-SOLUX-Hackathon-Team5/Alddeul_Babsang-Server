package com.example.alddeul_babsang.favorites.service;

import com.example.alddeul_babsang.entity.Favorite;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.User;
import com.example.alddeul_babsang.favorites.dto.FavoriteStoreDetailDto;
import com.example.alddeul_babsang.favorites.repository.FavoriteRepository;
import com.example.alddeul_babsang.favorites.repository.StoreRepository;
import com.example.alddeul_babsang.favorites.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    @Autowired
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final  StoreRepository storeRepository;
    User findUserById(int id)
    {     User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 user 찾을 수 없음. ID: " + id));
        return user;
    }
    public Optional<Favorite> checkIfFavoriteExists(User user, Store store) {
        Optional<Favorite> favorite=favoriteRepository.findByUserIdAndStoreId(user.getId(), store.getId());
        return favorite;
    }


    public List<FavoriteStoreDetailDto> getFavoritesByUser(int userId) {
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
