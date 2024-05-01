package com.plantapp.api.core.controller.post;

import com.plantapp.api.core.model.dto.PostDto;
import com.plantapp.api.core.model.request.CreatePostRequest;
import com.plantapp.api.core.model.response.APIResponse;
import com.plantapp.api.core.model.response.PagedResponse;
import com.plantapp.api.core.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;

@Tag(name = "Community API", description = "Community post api endpoints.")
@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Gets all community posts.")
    @GetMapping(path = "/")
    public APIResponse<PagedResponse<PostDto>> getAllPosts(@Nullable @RequestParam("user_id") String userId, Pageable pageable) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), postService.getAllPosts(pageable, userId));
    }

    @Operation(summary = "Gets a community post with id.")
    @GetMapping(value = "/{id}")
    public APIResponse<PostDto> getPost(@NotNull @PathVariable Long id, @Nullable @RequestParam("user_id") String userId) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), postService.getPostById(id, userId));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Creates a new community post.")
    @PostMapping(path = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<PostDto> savePost(@ModelAttribute @Valid CreatePostRequest createPostRequest) {
        return new APIResponse<>(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(), postService.createPost(createPostRequest));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Deletes a community post with id.")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@NotNull @PathVariable Long id) {
        postService.deletePostById(id);
    }
}
