package com.likelion.pbl.week11.assignment.repository;

import com.likelion.pbl.week11.assignment.domain.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Override
    @EntityGraph(attributePaths = "member")
    List<Assignment> findAll();

    @EntityGraph(attributePaths = "member")
    List<Assignment> findByMemberId(Long memberId);

    @EntityGraph(attributePaths = "member")
    Page<Assignment> findByMemberId(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    List<Assignment> findByTitleContaining(String keyword);

    @EntityGraph(attributePaths = "member")
    Page<Assignment> findByTitleContaining(String keyword, Pageable pageable);
}
