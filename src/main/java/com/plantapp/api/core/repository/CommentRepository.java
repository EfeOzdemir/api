package com.plantapp.api.core.repository;

import com.plantapp.api.core.entity.Comment;
import com.plantapp.api.core.model.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value =
            "SELECT new com.plantapp.api.core.model.dto.CommentDto(c.id, c.text, " +
            "new com.plantapp.api.core.model.dto.UserDto(c.user.id, c.user.username)) FROM Comment c")
    Page<CommentDto> findAllById(Long id, Pageable pageable);
}
