package com.plantapp.api.core.model.dto;

import com.plantapp.api.core.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Instant createdAt;
    private UserDto createdBy;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;

    public PostDto(Long id, String title, String content, String imageUrl, Instant createdAt, Integer likeCount, Integer commentCount, Boolean isLiked, String userId, String username) {
        this(id, title, content, imageUrl, createdAt, new UserDto(userId, username), likeCount, commentCount, isLiked);
    }

    public static PostDto fromPost(Post post) {
        return new PostDto(post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                new UserDto(post.getCreatedBy().getId(), post.getCreatedBy().getUsername()),
                0, 0, false);
    }
}
