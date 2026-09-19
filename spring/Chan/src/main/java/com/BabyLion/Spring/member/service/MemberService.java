package com.BabyLion.Spring.member.service;

import com.BabyLion.Spring.global.dto.PageResponse;
import com.BabyLion.Spring.global.exception.*;
import com.BabyLion.Spring.global.util.SecurityUtil;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.domain.RoleType;
import com.BabyLion.Spring.member.dto.*;
import com.BabyLion.Spring.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {
    // 인터페이스에 의존 (구현체에 의존하지 않음)
    private final MemberRepository repository;

    // 생성자를 통해 의존성 주입
    @Autowired
    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Member createLion(LionCreateRequest dto){
        if (repository.existsByName(dto.getName())) {
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_MEMBER_NAME);
        }

        Member member = new Member(
                dto.getName(),
                dto.getMajor(),
                dto.getPart(),
                dto.getGeneration(),
                RoleType.LION,
                dto.getStudentId(),
                null, null,null);

        return repository.save(member);
    }

    @Transactional
    public Member createStaff(StaffCreateRequest dto){

        if(repository.existsByName(dto.getName())){
            throw new BusinessException(ErrorCodeEnum.DUPLICATE_MEMBER_NAME);
        }

        Member member = new Member(
                dto.getName(),
                dto.getMajor(),
                dto.getPart(),
                dto.getGeneration(),
                RoleType.STAFF,
                null,
                dto.getPosition(), null, null);
        return repository.save(member);
    }

    @Transactional
    public Member updateLion(Long id, LionUpdateRequest dto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));

        if (!member.getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_FORBIDDEN);
        }
        member.updateInfo(dto.getName(), dto.getMajor(), dto.getPart(), dto.getGeneration());
        member.updateStudentID(dto.getStudentId());
        return repository.save(member);
    }

    @Transactional
    public Member updateStaff(Long id, StaffUpdateRequest dto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));

        if (!member.getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_FORBIDDEN);
        }
        member.updateInfo(dto.getName(), dto.getMajor(), dto.getPart(), dto.getGeneration());
        member.updatePosition(dto.getPosition());
        return repository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Member member = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));

        if (!member.getId().equals(currentMemberId)) {
            throw new BusinessException(ErrorCodeEnum.MEMBER_FORBIDDEN);
        }

        repository.delete(member);
    }

    public Member searchById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.MEMBER_NOT_FOUND));
    }

    public PageResponse<MemberResponse> getAllMembers(Pageable pageable) {
        Page<Member> memberPage = repository.findAll(pageable);
        Page<MemberResponse> dtoPage = memberPage.map(MemberResponse::from);
        PageResponse<MemberResponse> result = PageResponse.from(dtoPage);
        return result;
    }

    public List<Member> findByPart(String part){
        return repository.findByPart(part);
    }
}
