package com.likelion.pbl.week11.comment.service;

import com.likelion.pbl.week11.assignment.domain.Assignment;
import com.likelion.pbl.week11.assignment.repository.AssignmentRepository;
import com.likelion.pbl.week11.comment.domain.Comment;
import com.likelion.pbl.week11.comment.dto.CommentCreateRequest;
import com.likelion.pbl.week11.comment.dto.CommentResponse;
import com.likelion.pbl.week11.comment.repository.CommentRepository;
import com.likelion.pbl.week11.domain.Member;
import com.likelion.pbl.week11.global.exception.*;
import com.likelion.pbl.week11.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    public CommentService(CommentRepository commentRepository,
                          AssignmentRepository assignmentRepository,
                          MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.assignmentRepository = assignmentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public CommentResponse createComment(Long assignmentId, CommentCreateRequest request) {
        Long currentMemberId = getCurrentMemberId();
        Member member = getMember(currentMemberId);
        Assignment assignment = getAssignment(assignmentId);

        Comment comment = new Comment(request.getContent(), assignment, member);
        return CommentResponse.from(commentRepository.save(comment));
    }

    public List<CommentResponse> getComments(Long assignmentId) {
        getAssignment(assignmentId);
        return commentRepository.findByAssignmentIdOrderByIdDesc(assignmentId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Long currentMemberId = getCurrentMemberId();
        Comment comment = getComment(commentId);

        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new ForbiddenException(ErrorCodeEnum.COMMENT_FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found. id: " + memberId));
    }

    private Assignment getAssignment(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found. id: " + assignmentId));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found. id: " + commentId));
    }

    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationFailedException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Long memberId)) {
            throw new AuthenticationFailedException("유효하지 않은 인증 정보입니다.");
        }

        return memberId;
    }
}
