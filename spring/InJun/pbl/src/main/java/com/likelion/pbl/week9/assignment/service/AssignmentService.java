package com.likelion.pbl.week9.assignment.service;

import com.likelion.pbl.week9.assignment.domain.Assignment;
import com.likelion.pbl.week9.assignment.dto.AssignmentCreateRequest;
import com.likelion.pbl.week9.assignment.dto.AssignmentUpdateRequest;
import com.likelion.pbl.week9.assignment.repository.AssignmentRepository;
import com.likelion.pbl.week9.domain.Member;
import com.likelion.pbl.week9.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, MemberRepository memberRepository) {
        this.assignmentRepository = assignmentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Assignment createAssignment(Long memberId, AssignmentCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            return null;
        }

        Assignment assignment = new Assignment(request.getTitle(), request.getDescription(), member);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> findByMemberId(Long memberId) {
        return assignmentRepository.findByMemberId(memberId);
    }

    public Assignment findById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    @Transactional
    public Assignment updateAssignment(Long id, AssignmentUpdateRequest request) {
        Assignment assignment = assignmentRepository.findById(id).orElse(null);

        if (assignment == null) {
            return null;
        }

        assignment.updateInfo(request.getTitle(), request.getDescription());
        return assignment;
    }

    @Transactional
    public boolean deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            return false;
        }

        assignmentRepository.deleteById(id);
        return true;
    }

    public List<Assignment> searchByTitle(String title) {
        return assignmentRepository.searchByTitle(title);
    }
}
