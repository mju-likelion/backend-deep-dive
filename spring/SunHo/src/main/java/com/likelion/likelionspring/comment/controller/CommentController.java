package com.likelion.likelionspring.comment.controller;

import com.likelion.likelionspring.comment.dto.CommentCreateRequest;
import com.likelion.likelionspring.comment.dto.CommentResponse;
import com.likelion.likelionspring.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long assignmentId,
            @AuthenticationPrincipal Long memberId,
            @RequestBody CommentCreateRequest request) {
        CommentResponse response = new CommentResponse(
                commentService.save(assignmentId, memberId, request.getContent()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<List<CommentResponse>> findByAssignmentId(@PathVariable Long assignmentId) {
        List<CommentResponse> responses = commentService.findByAssignmentId(assignmentId).stream()
                .map(CommentResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Long memberId) {
        commentService.delete(id, memberId);
        return ResponseEntity.noContent().build();
    }
}