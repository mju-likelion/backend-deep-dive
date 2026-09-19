package com.BabyLion.Spring.comment.dto;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.comment.domain.Comment;
import com.BabyLion.Spring.member.domain.Member;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String memberName;
    private Long assignmentId;

    public CommentResponse(Long id, String content, LocalDateTime createdAt, String memberName, Long assignmentId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.memberName = memberName;
        this.assignmentId = assignmentId;
    }

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getMember().getName(),
                comment.getAssignment().getId());
    }
}
