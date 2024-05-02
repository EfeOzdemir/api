package com.plantapp.api.core.controller.post;

import com.plantapp.api.core.model.dto.UserDto;
import com.plantapp.api.core.model.response.APIResponse;
import com.plantapp.api.core.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Get users who likes the community post with id.")
    @GetMapping(value = "/{id}/likes")
    public APIResponse<List<UserDto>> getLikesOfPost(@NotNull @PathVariable Long id) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                likeService.getUsersWhoLikePostById(id));
    }

    @SecurityRequirement(name = "jwt-token")
    @Operation(summary = "Likes a community post if it is not liked, unlikes if it is liked.")
    @PostMapping(value = "/{id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public APIResponse<Void> likePost(@NotNull @PathVariable Long id) {
        String status = likeService.likePostById(id);
        return new APIResponse<>(HttpStatus.NO_CONTENT.value(), status, null);
    }
}
