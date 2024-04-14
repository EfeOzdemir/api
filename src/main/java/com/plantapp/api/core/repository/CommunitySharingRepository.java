package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.model.CSharingDto;
import com.plantapp.api.core.model.UserDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunitySharingRepository extends JpaRepository<CommunitySharing, Long> {
    @Query(value = "SELECT new com.plantapp.api.core.model.UserDto(u.id, u.username) FROM CommunitySharing cs JOIN cs.usersWhoLike u")
    List<UserDto> findUsersWhoLike(Long id);
    @Query(nativeQuery = true, name = "findAllSharing")
    List<CSharingDto> findAllSharing(@Param("userId") String userId);

    @EntityGraph(attributePaths = {"comments", "comments.user"})
    Optional<CommunitySharing> findById(Long id);
}

