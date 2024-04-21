package com.plantapp.api.core.repository;

import com.plantapp.api.core.model.dto.UserDto;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDto> getUsersWhoLike(@NonNull Long postId) {
        var query = entityManager.createNativeQuery(
                """ 
                SELECT user.id, user.username
                FROM community_likes likes
                JOIN users user ON user.id = likes.user_id
                WHERE likes.community_sharing_id = :postId
                """, Tuple.class)
                .setParameter("postId", postId)
                .setMaxResults(100);
        List<Tuple> result = query.getResultList();
        return result.stream().map(i -> new UserDto(String.valueOf(i.get(0)), String.valueOf(i.get(1)))).toList();
    }
}
