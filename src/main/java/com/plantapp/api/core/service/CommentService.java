package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.Comment;
import com.plantapp.api.core.entity.CommunitySharing;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.model.request.CommentRequest;
import com.plantapp.api.core.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final EntityManager entityManager;
    private final CommentRepository commentRepository;

    public void comment(CommentRequest commentRequest, Long id) {
        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = entityManager.getReference(User.class, Objects.requireNonNull(uid));
        CommunitySharing communitySharing = entityManager.getReference(CommunitySharing.class, id);
        Comment comment = Comment.builder().text(commentRequest.text()).user(user).communitySharing(communitySharing).build();
        commentRepository.save(comment);
    }
}
