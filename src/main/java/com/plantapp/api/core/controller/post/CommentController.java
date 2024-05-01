package com.plantapp.api.core.controller.post;

import com.plantapp.api.core.model.dto.CommentDto;
import com.plantapp.api.core.model.request.CommentRequest;
import com.plantapp.api.core.model.response.APIResponse;
import com.plantapp.api.core.model.response.PagedResponse;
import com.plantapp.api.core.service.CommentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public APIResponse<PagedResponse<CommentDto>> getComments(@NonNull @PathVariable Long id, Pageable pageable) {
        return new APIResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), commentService.getComments(id, pageable));
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public void comment(@RequestBody CommentRequest text, @NonNull @PathVariable Long id) {
        commentService.comment(text, id);
    }
}
