package com.BabyLion.Spring.comment.service;

import com.BabyLion.Spring.assignment.domain.Assignment;
import com.BabyLion.Spring.assignment.repository.AssignmentRepository;
import com.BabyLion.Spring.comment.domain.Comment;
import com.BabyLion.Spring.comment.dto.CommentCreateRequest;
import com.BabyLion.Spring.comment.dto.CommentResponse;
import com.BabyLion.Spring.comment.repository.CommentRepository;
import com.BabyLion.Spring.global.exception.BusinessException;
import com.BabyLion.Spring.global.exception.ErrorCodeEnum;
import com.BabyLion.Spring.global.util.SecurityUtil;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final MemberRepository memberRepository;
    private final AssignmentRepository assignmentRepository;
    private final CommentRepository commentRepository;

    public CommentService(MemberRepository memberRepository, AssignmentRepository assignmentRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.assignmentRepository = assignmentRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public CommentResponse createComment(Long assignmentId, CommentCreateRequest dto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ASSIGNMENT_NOT_FOUND));

        Comment comment = new Comment(null, dto.getContent(), LocalDateTime.now(), member, assignment);
        return CommentResponse.from(commentRepository.save(comment));
    }

    public List<CommentResponse> getComments(Long assignmentId) {
        assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.ASSIGNMENT_NOT_FOUND));

        return commentRepository.findByAssignmentId(assignmentId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void deleteComment(Long id){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.COMMENT_NOT_FOUND));

        if (!target.getMember().getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.COMMENT_FORBIDDEN);
        }

        commentRepository.delete(target);
    }
}
