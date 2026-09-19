package com.BabyLion.Spring.comment.repository;

import com.BabyLion.Spring.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAssignmentId(Long assignmentId);
}
