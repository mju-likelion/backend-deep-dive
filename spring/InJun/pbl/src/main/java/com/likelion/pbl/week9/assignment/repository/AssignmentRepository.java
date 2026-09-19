package com.likelion.pbl.week9.assignment.repository;

import com.likelion.pbl.week9.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByMemberId(Long memberId);

    @Query("select a from Assignment a where a.title like concat('%', :title, '%')")
    List<Assignment> searchByTitle(@Param("title") String title);
}
