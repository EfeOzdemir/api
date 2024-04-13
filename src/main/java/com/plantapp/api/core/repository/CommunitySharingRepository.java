package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.model.projections.CommunitySharingProjection;
import com.plantapp.api.core.model.projections.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommunitySharingRepository extends JpaRepository<CommunitySharing, Long> {
    @Query("SELECT u.id as id, u.username as username FROM CommunitySharing cs JOIN cs.usersWhoLike u WHERE cs.id = :id")
    List<UserProjection> findUsersWhoLikeById(Long id);

    @Query("SELECT cs.id as id, cs.title as title, cs.content as content, cs.imageUrl as imageUrl, cs.createdAt as createdAt, " +
            "cs.updatedAt as updatedAt, cs.createdBy as createdBy, size(cs.usersWhoLike) as likeCount FROM CommunitySharing cs")
    List<CommunitySharingProjection> findAllListing();

    @Query("SELECT cs.id as id, cs.title as title, cs.content as content, cs.imageUrl as imageUrl, cs.createdAt as createdAt, " +
            "cs.updatedAt as updatedAt, cs.createdBy as createdBy, size(cs.usersWhoLike) as likeCount FROM CommunitySharing cs WHERE cs.id = :id")
    Optional<CommunitySharingProjection> findByIdWithProjection(Long id);
}

