package com.plantapp.api.core.controller;

import com.plantapp.api.core.model.request.CommentRequest;
import com.plantapp.api.core.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void comment(@RequestBody CommentRequest text, @PathVariable Long id) {
        commentService.comment(text, id);
    }
}
