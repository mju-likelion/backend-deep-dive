package com.likelion.likelionspring.assignment.service;

import com.likelion.likelionspring.assignment.domain.Assignment;
import com.likelion.likelionspring.member.domain.Member;
import com.likelion.likelionspring.assignment.repository.AssignmentRepository;
import com.likelion.likelionspring.global.exception.AssignmentNotFoundException;
import com.likelion.likelionspring.global.exception.ErrorCodeEnum;
import com.likelion.likelionspring.global.exception.ForbiddenException;
import com.likelion.likelionspring.global.exception.MemberNotFoundException;
import com.likelion.likelionspring.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

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

    // 과제 등록
    @Transactional
    public Assignment save(Long memberId, String title, String description) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다: " + memberId));
        Assignment assignment = new Assignment(title, description, member);
        return assignmentRepository.save(assignment);
    }

    // 멤버별 과제 조회
    public List<Assignment> findByMemberId(Long memberId) {
        return assignmentRepository.findByMemberId(memberId);
    }

    // 단건 조회
    public Assignment findById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("과제를 찾을 수 없습니다: " + id));
    }

    // 수정
    @Transactional
    public Assignment update(Long id, Long currentMemberId, String title, String description) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("과제를 찾을 수 없습니다: " + id));
        validateOwner(assignment, currentMemberId);
        assignment.updateInfo(title, description);
        return assignmentRepository.save(assignment);
    }

    // 삭제
    @Transactional
    public void delete(Long id, Long currentMemberId) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("과제를 찾을 수 없습니다: " + id));
        validateOwner(assignment, currentMemberId);
        assignmentRepository.delete(assignment);
    }

    private void validateOwner(Assignment assignment, Long currentMemberId) {
        if (!assignment.getMember().getId().equals(currentMemberId)) {
            throw new ForbiddenException(ErrorCodeEnum.FORBIDDEN);
        }
    }

    // 전체 조회 (페이징)
    public Page<Assignment> findAll(Pageable pageable) {
        return assignmentRepository.findAll(pageable);
    }

    // 제목 키워드 검색
    public List<Assignment> searchByKeyword(String keyword) {
        return assignmentRepository.findByTitleContaining(keyword);
    }
}

