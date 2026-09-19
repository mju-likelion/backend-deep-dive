package com.likelion.likelionspring.comment.dto;

import com.likelion.likelionspring.comment.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private String content;
    private Long assignmentId;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.assignmentId = comment.getAssignment().getId();
        this.memberId = comment.getMember().getId();
        this.memberName = comment.getMember().getName();
        this.createdAt = comment.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}