package com.plantapp.api.core.model.dto;

public record CommentDto(Long id, String text, UserDto user) {
}
