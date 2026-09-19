package com.likelion.pbl.week11.comment.dto;

import com.likelion.pbl.week11.comment.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse {

    private final Long id;
    private final String content;
    private final Long assignmentId;
    private final Long memberId;
    private final String memberName;
    private final LocalDateTime createdAt;

    public CommentResponse(Long id, String content, Long assignmentId,
                           Long memberId, String memberName, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.assignmentId = assignmentId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAssignment().getId(),
                comment.getMember().getId(),
                comment.getMember().getName(),
                comment.getCreatedAt()
        );
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
