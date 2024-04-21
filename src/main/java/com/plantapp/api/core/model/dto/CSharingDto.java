package com.plantapp.api.core.model.dto;

import com.plantapp.api.core.entity.CommunitySharing;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CSharingDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Instant createdAt;
    private UserDto createdBy;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;

    public CSharingDto(Long id, String title, String content, String imageUrl, Instant createdAt, Integer likeCount, Integer commentCount, Boolean isLiked, String userId, String username) {
        this(id, title, content, imageUrl, createdAt, new UserDto(userId, username), likeCount, commentCount, isLiked);
    }

    public static CSharingDto fromCommunitySharing(CommunitySharing communitySharing) {
        return new CSharingDto(communitySharing.getId(),
                communitySharing.getTitle(),
                communitySharing.getContent(),
                communitySharing.getImageUrl(),
                communitySharing.getCreatedAt(),
                new UserDto(communitySharing.getCreatedBy().getId(), communitySharing.getCreatedBy().getUsername()),
                0, 0, false);
    }
}
