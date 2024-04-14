package com.plantapp.api.core.model;

import com.plantapp.api.core.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private String text;
    private UserDto user;

    public static CommentDto of(Comment comment) {
        return new CommentDto(
                comment.getText(),
                new UserDto(comment.getUser().getId(), comment.getUser().getUsername())
        );
    }
}
