package com.likelion.pbl.week11.service;

import com.likelion.pbl.week11.domain.Member;
import com.likelion.pbl.week11.domain.RoleType;
import com.likelion.pbl.week11.dto.LionCreateRequest;
import com.likelion.pbl.week11.dto.LionUpdateRequest;
import com.likelion.pbl.week11.dto.StaffCreateRequest;
import com.likelion.pbl.week11.dto.StaffUpdateRequest;
import com.likelion.pbl.week11.global.exception.DuplicateMemberException;
import com.likelion.pbl.week11.global.exception.MemberNotFoundException;
import com.likelion.pbl.week11.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createLion(LionCreateRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            throw new DuplicateMemberException("이미 존재하는 이름입니다. name: " + request.getName());
        }

        Member member = new Member(
                request.getName(),
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                RoleType.LION,
                request.getStudentId(),
                null
        );

        return memberRepository.save(member);
    }

    @Transactional
    public Member createStaff(StaffCreateRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            throw new DuplicateMemberException("이미 존재하는 이름입니다. name: " + request.getName());
        }

        Member member = new Member(
                request.getName(),
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                RoleType.STAFF,
                null,
                request.getPosition()
        );

        return memberRepository.save(member);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Page<Member> findAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public List<Member> findByPart(String part) {
        return memberRepository.findByPart(part);
    }

    public Page<Member> findByPart(String part, Pageable pageable) {
        return memberRepository.findByPart(part, pageable);
    }

    public Member findMember(Long id) {
        return getMember(id);
    }

    @Transactional
    public Member updateLion(Long id, LionUpdateRequest request) {
        Member member = getMember(id);

        member.updateInfo(request.getMajor(), request.getGeneration(), request.getPart());
        member.updateStudentId(request.getStudentId());
        member.updatePosition(null);

        return memberRepository.save(member);
    }

    @Transactional
    public Member updateStaff(Long id, StaffUpdateRequest request) {
        Member member = getMember(id);

        member.updateInfo(request.getMajor(), request.getGeneration(), request.getPart());
        member.updateStudentId(null);
        member.updatePosition(request.getPosition());

        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = getMember(id);
        memberRepository.delete(member);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다. id: " + id));
    }
}
