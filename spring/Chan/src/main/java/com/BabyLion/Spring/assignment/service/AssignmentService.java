package com.BabyLion.Spring.assignment.service;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.assignment.dto.AssignmentCreateRequest;
import com.BabyLion.Spring.assignment.dto.AssignmentResponse;
import com.BabyLion.Spring.assignment.dto.AssignmentUpdateRequest;
import com.BabyLion.Spring.assignment.repository.AssignmentRepository;
import com.BabyLion.Spring.global.dto.PageResponse;
import com.BabyLion.Spring.global.exception.BusinessException;
import com.BabyLion.Spring.global.exception.ErrorCodeEnum;
import com.BabyLion.Spring.global.util.SecurityUtil;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.repository.MemberRepository;
import com.BabyLion.Spring.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, MemberService memberService, MemberRepository memberRepository) {
        this.assignmentRepository = assignmentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Assignment createAssignment(Long memberId, AssignmentCreateRequest dto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));

        Assignment assignment = new Assignment(dto.getTitle(), dto.getDescription(), member);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> memberAssignment(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));
        return assignmentRepository.findByMemberId(memberId);
    }

    public Assignment searchAssignment(Long id){
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ASSIGNMENT_NOT_FOUND));
    }

    public List<Assignment> searchByTitle(String keyword) {
        return assignmentRepository.findByTitleContaining(keyword);
    }


    @Transactional
    public Assignment updateAssignment(Long id, AssignmentUpdateRequest dto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Assignment target = assignmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ASSIGNMENT_NOT_FOUND));

        if (!target.getMember().getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.ASSIGNMENT_FORBIDDEN);
        }

        // 4. 수정
        target.updateInfo(dto.getTitle(), dto.getDescription());
        return assignmentRepository.save(target);
    }

    @Transactional
    public void deleteAssignment(Long id){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Assignment target = assignmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ASSIGNMENT_NOT_FOUND));

        if (!target.getMember().getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.ASSIGNMENT_FORBIDDEN);
        }

        assignmentRepository.delete(target);
    }

    public PageResponse<AssignmentResponse> getAllAssignments(Pageable pageable){
        Page<Assignment> assignmentPage = assignmentRepository.findAll(pageable);
        Page<AssignmentResponse> dtopages = assignmentPage.map(AssignmentResponse :: from);
        PageResponse<AssignmentResponse> result = PageResponse.from(dtopages);
        return result;
    }
}
