package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.model.dto.CSharingDto;
import com.plantapp.api.core.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunitySharingRepository extends JpaRepository<CommunitySharing, Long> {

    @Query(value = "SELECT u.id AS id, u.username AS username " +
            "FROM community_likes cl " +
            "JOIN users u ON cl.user_id = u.id " +
            "WHERE cl.community_sharing_id = :sharingId", nativeQuery = true)
    List<UserDto> findUsersWhoLikeById(@Param("sharingId") Long sharingId);
    @Query(name = "findAllSharing", nativeQuery = true)
    Page<CSharingDto> findAllSharing(@Param("userId") String userId, Pageable pageable);

    @Query(name = "findSharingById", nativeQuery = true)
    Optional<CSharingDto> findById(@Param("postId") Long postId, @Param("userId") String userId);

    @Query(value = "SELECT true FROM CommunitySharing cs WHERE cs.id = :id")
    boolean isExistsById(Long id);

    Integer deleteByIdAndCreatedById(Long id, String createdById);

    @EntityGraph(attributePaths = {"usersWhoLike"})
    Optional<CommunitySharing> findLikesById(Long id);
}