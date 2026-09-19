package com.likelion.pbl.week11.comment.controller;

import com.likelion.pbl.week11.comment.dto.CommentCreateRequest;
import com.likelion.pbl.week11.comment.dto.CommentResponse;
import com.likelion.pbl.week11.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import jakarta.validation.Valid;
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(assignmentId, request));
    }

    @GetMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(commentService.getComments(assignmentId));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
