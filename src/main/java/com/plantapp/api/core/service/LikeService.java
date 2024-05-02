package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.Post;
import com.plantapp.api.core.entity.User;
import com.plantapp.api.core.exception.PostNotFoundException;
import com.plantapp.api.core.model.dto.UserDto;
import com.plantapp.api.core.repository.PostRepository;
import com.plantapp.api.core.repository.LikeRepository;
import com.plantapp.api.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public List<UserDto> getUsersWhoLikePostById(Long id) {
        if(!postRepository.isExistsById(id))
            throw new PostNotFoundException("Community post with id not found!");
        return likeRepository.getUsersWhoLike(id);
    }

    @Transactional
    public String likePostById(Long id) {
        Post post = postRepository.findLikesById(id)
                .orElseThrow(() -> new PostNotFoundException("Community post with id not found!"));

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(userId).get();

        if(post.isLikedBy(user)) {
            post.unlikeBy(user);
            return "liked";
        }
        else {
            post.likeBy(user);
            return "unliked";
        }
    }
}
