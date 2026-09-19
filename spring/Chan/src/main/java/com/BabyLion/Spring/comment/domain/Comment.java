package com.BabyLion.Spring.comment.domain;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String content;
    LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;
    @ManyToOne
    @JoinColumn(name = "Assignment_id")
    Assignment assignment;
}
