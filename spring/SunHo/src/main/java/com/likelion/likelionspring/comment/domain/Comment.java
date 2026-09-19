package com.likelion.likelionspring.comment.domain;

import com.likelion.likelionspring.assignment.domain.Assignment;
import com.likelion.likelionspring.member.domain.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createdAt;

    protected Comment() {}

    public Comment(String content, Assignment assignment, Member member) {
        this.content = content;
        this.assignment = assignment;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}