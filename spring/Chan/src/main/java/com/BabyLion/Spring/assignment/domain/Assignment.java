package com.BabyLion.Spring.assignment.domain;

import com.BabyLion.Spring.comment.domain.Comment;
import com.BabyLion.Spring.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    protected Assignment(){}

    public Assignment(String title, String description, Member member){
        this.title = title;
        this.description = description;
        this.member = member;
    }

    public void updateInfo(String title, String description){
        this.title = title;
        this.description = description;
    }
}
