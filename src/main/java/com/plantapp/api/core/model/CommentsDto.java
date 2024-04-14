package com.plantapp.api.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentsDto {
    private List<CommentDto> comments;
    private boolean hasNext;
    private int commentCount;
}
