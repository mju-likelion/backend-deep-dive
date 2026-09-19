package com.likelion.likelionspring.comment.repository;

import com.likelion.likelionspring.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAssignmentId(Long assignmentId);
}