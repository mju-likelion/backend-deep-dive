package com.BabyLion.Spring.comment.controller;


import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.assignment.dto.AssignmentResponse;
import com.BabyLion.Spring.comment.domain.Comment;
import com.BabyLion.Spring.comment.dto.CommentCreateRequest;
import com.BabyLion.Spring.comment.dto.CommentResponse;
import com.BabyLion.Spring.comment.service.CommentService;
import com.BabyLion.Spring.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Comment", description = "댓글 관리 API")
@RestController
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 등록", description = "댓글을 등록합니다.")
    @PostMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<?> createcomment(@PathVariable Long assignmentId, @RequestBody CommentCreateRequest dto) {
        CommentResponse comment = commentService.createComment(assignmentId, dto);
        return ResponseEntity.status(201).body(comment);
    }

    @Operation(summary = "댓글 목록 조회", description = "댓글을 조회합니다.")
    @GetMapping("/assignments/{assignmentId}/comments")
    public ResponseEntity<List<CommentResponse>> getComment(@PathVariable Long assignmentId) {
        List<CommentResponse> comments = commentService.getComments(assignmentId);
        return ResponseEntity.ok(comments);
    }


    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deletecomment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(204).build();
    }

}
