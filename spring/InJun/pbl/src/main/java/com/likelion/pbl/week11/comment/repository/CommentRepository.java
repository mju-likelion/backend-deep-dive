package com.likelion.pbl.week11.comment.repository;

import com.likelion.pbl.week11.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAssignmentIdOrderByIdDesc(Long assignmentId);
}
