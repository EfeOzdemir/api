package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.Post;
import com.plantapp.api.core.model.dto.PostDto;
import com.plantapp.api.core.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT u.id AS id, u.username AS username " +
            "FROM community_likes cl " +
            "JOIN users u ON cl.user_id = u.id " +
            "WHERE cl.community_post_id = :postId", nativeQuery = true)
    List<UserDto> findUsersWhoLikeById(@Param("postId") Long postId);
    @Query(name = "findAllPosts", nativeQuery = true)
    Page<PostDto> findAllPosts(@Param("userId") String userId, Pageable pageable);

    @Query(name = "findPostById", nativeQuery = true)
    Optional<PostDto> findById(@Param("postId") Long postId, @Param("userId") String userId);

    @Query(value = "SELECT true FROM Post cs WHERE cs.id = :id")
    boolean isExistsById(Long id);

    Integer deleteByIdAndCreatedById(Long id, String createdById);

    @EntityGraph(attributePaths = {"usersWhoLike"})
    Optional<Post> findLikesById(Long id);
}