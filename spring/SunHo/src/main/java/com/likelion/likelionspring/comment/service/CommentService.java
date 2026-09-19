package com.likelion.likelionspring.comment.service;

import com.likelion.likelionspring.assignment.domain.Assignment;
import com.likelion.likelionspring.assignment.repository.AssignmentRepository;
import com.likelion.likelionspring.comment.domain.Comment;
import com.likelion.likelionspring.comment.repository.CommentRepository;
import com.likelion.likelionspring.member.domain.Member;
import com.likelion.likelionspring.global.exception.AssignmentNotFoundException;
import com.likelion.likelionspring.global.exception.CommentNotFoundException;
import com.likelion.likelionspring.global.exception.ErrorCodeEnum;
import com.likelion.likelionspring.global.exception.ForbiddenException;
import com.likelion.likelionspring.global.exception.MemberNotFoundException;
import com.likelion.likelionspring.member.repository.MemberRepository;
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

    // 댓글 작성
    @Transactional
    public Comment save(Long assignmentId, Long memberId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("과제를 찾을 수 없습니다: " + assignmentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다: " + memberId));
        Comment comment = new Comment(content, assignment, member);
        return commentRepository.save(comment);
    }

    // 과제별 댓글 목록 조회
    public List<Comment> findByAssignmentId(Long assignmentId) {
        assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("과제를 찾을 수 없습니다: " + assignmentId));
        return commentRepository.findByAssignmentId(assignmentId);
    }

    // 삭제 (작성자 본인만)
    @Transactional
    public void delete(Long id, Long currentMemberId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다: " + id));
        if (!comment.getMember().getId().equals(currentMemberId)) {
            throw new ForbiddenException(ErrorCodeEnum.FORBIDDEN);
        }
        commentRepository.deleteById(id);
    }
}