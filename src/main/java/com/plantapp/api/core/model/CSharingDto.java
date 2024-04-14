package com.plantapp.api.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import lombok.Data;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.List;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class CSharingDto {

    public CSharingDto(Long id, String title, String content, String imageUrl, Instant createdAt, Integer likeCount, Integer commentCount,
                       Boolean isLiked, String userId, String username, CommentsDto commentDto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isLiked = isLiked;
        this.createdBy = new UserDto(userId, username);
        this.commentData = commentDto;
    }

    public CSharingDto(Long id, String title, String content, String imageUrl, Instant createdAt, String userId, String username) {
        this(id, title, content, imageUrl, createdAt, 0,0, false, userId, username, null);
    }

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Instant createdAt;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;
    private UserDto createdBy;
    private CommentsDto commentData;

    public static CSharingDto of(CommunitySharing communitySharing, User user) {
        int likeCnt =  Hibernate.size(communitySharing.getUsersWhoLike());
        int commentCount = Hibernate.size(communitySharing.getComments());
        boolean isLiked = Hibernate.contains(communitySharing.getUsersWhoLike(), user);
        List<CommentDto> comments = communitySharing.getComments().subList(0, 4).stream().map(CommentDto::of).toList();
        CommentsDto commentsDto = new CommentsDto(comments, commentCount > 4, commentCount);
        return new CSharingDto(communitySharing.getId(), communitySharing.getTitle(), communitySharing.getContent(),
                communitySharing.getImageUrl(), communitySharing.getCreatedAt(), likeCnt, commentCount, isLiked,
                communitySharing.getCreatedBy().getId(), communitySharing.getCreatedBy().getUsername(), commentsDto);
    }

    public static CSharingDto ofWithDefaults(CommunitySharing communitySharing) {
        return new CSharingDto(communitySharing.getId(), communitySharing.getTitle(), communitySharing.getContent(),
                communitySharing.getImageUrl(), communitySharing.getCreatedAt(),
                communitySharing.getCreatedBy().getId(), communitySharing.getCreatedBy().getUsername());
    }
}
