package com.likelion.likelionspring.assignment.domain;

import com.likelion.likelionspring.comment.domain.Comment;
import com.likelion.likelionspring.member.domain.Member;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Assignment(){}

    public Assignment(String title, String description, Member member) {
        this.title = title;
        this.description = description;
        this.member = member;
    }


    public void updateInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Member getMember() {
        return member;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
