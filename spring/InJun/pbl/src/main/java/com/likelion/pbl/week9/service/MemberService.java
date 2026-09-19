package com.likelion.pbl.week9.service;

import com.likelion.pbl.week9.domain.Member;
import com.likelion.pbl.week9.domain.RoleType;
import com.likelion.pbl.week9.dto.LionCreateRequest;
import com.likelion.pbl.week9.dto.LionUpdateRequest;
import com.likelion.pbl.week9.dto.StaffCreateRequest;
import com.likelion.pbl.week9.dto.StaffUpdateRequest;
import com.likelion.pbl.week9.repository.MemberRepository;
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
            return null;
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
            return null;
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

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Transactional
    public Member updateLion(Long id, LionUpdateRequest request) {
        Member member = memberRepository.findById(id).orElse(null);

        if (member == null) {
            return null;
        }

        member.updateInfo(request.getMajor(), request.getGeneration(), request.getPart());
        member.updateStudentId(request.getStudentId());
        member.updatePosition(null);

        return memberRepository.save(member);
    }

    @Transactional
    public Member updateStaff(Long id, StaffUpdateRequest request) {
        Member member = memberRepository.findById(id).orElse(null);

        if (member == null) {
            return null;
        }

        member.updateInfo(request.getMajor(), request.getGeneration(), request.getPart());
        member.updateStudentId(null);
        member.updatePosition(request.getPosition());

        return memberRepository.save(member);
    }

    @Transactional
    public boolean deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            return false;
        }

        memberRepository.deleteById(id);
        return true;
    }
}
