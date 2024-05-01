package com.plantapp.api.core.service;

import com.plantapp.api.core.entity.Post;
import com.plantapp.api.core.exception.PostNotFoundException;
import com.plantapp.api.core.model.dto.PostDto;
import com.plantapp.api.core.model.request.CreatePostRequest;
import com.plantapp.api.core.model.response.PagedResponse;
import com.plantapp.api.core.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CloudStorageService cloudStorageService;
    private final PostRepository postRepository;

    public PagedResponse<PostDto> getAllPosts(Pageable pageable, String userId) {
        Page<PostDto> paged = postRepository.findAllPosts(userId, pageable);
        return PagedResponse.<PostDto>builder()
                .content(paged.getContent())
                .currentPage(paged.getNumber())
                .pageSize(paged.getSize())
                .totalPages(paged.getTotalPages())
                .hasNext(paged.getTotalPages() > (paged.getNumber() + 1))
                .build();
    }

    public PostDto getPostById(Long postId, String userId) {
        return postRepository.findById(postId, userId)
                .orElseThrow(() -> new PostNotFoundException("Community post not found!"));
    }

    public PostDto createPost(CreatePostRequest createPostRequest) {
        try {
            String imageKey = cloudStorageService.generateRandomKey();
            String imageURL = cloudStorageService.generatePublicUrl(imageKey);

            CompletableFuture<Boolean> uploadStatus = cloudStorageService.upload(createPostRequest.image(), imageKey);
            Post post = Post.builder()
                    .title(createPostRequest.title()).content(createPostRequest.content()).imageUrl(imageURL).build();
            post = postRepository.save(post);

            if (uploadStatus.join())
                return PostDto.fromPost(post);
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePostById(Long id) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer res = postRepository.deleteByIdAndCreatedById(id, user);

        if (res == 0) {
            throw new AccessDeniedException("Access denied. Unauthorized access.");
        }
    }

}
