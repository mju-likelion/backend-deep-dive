package com.likelion.pbl.week7.service;

import com.likelion.pbl.week7.domain.role.Lion;
import com.likelion.pbl.week7.domain.role.Role;
import com.likelion.pbl.week7.domain.role.Staff;
import com.likelion.pbl.week7.dto.LionCreateRequest;
import com.likelion.pbl.week7.dto.LionUpdateRequest;
import com.likelion.pbl.week7.dto.StaffCreateRequest;
import com.likelion.pbl.week7.dto.StaffUpdateRequest;
import com.likelion.pbl.week7.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Lion createLion(LionCreateRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            return null;
        }

        Lion lion = new Lion(
                request.getName(),
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                request.getStudentId()
        );

        memberRepository.save(lion);
        return lion;
    }

    public Staff createStaff(StaffCreateRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            return null;
        }

        Staff staff = new Staff(
                request.getName(),
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                request.getPosition()
        );

        memberRepository.save(staff);
        return staff;
    }

    public Role findMember(String name) {
        return memberRepository.findByName(name);
    }

    public Lion updateLion(String name, LionUpdateRequest request) {
        Role foundMember = memberRepository.findByName(name);

        if (foundMember == null) {
            return null;
        }

        Lion updatedLion = new Lion(
                name,
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                request.getStudentId()
        );

        memberRepository.updateByName(name, updatedLion);
        return updatedLion;
    }

    public Staff updateStaff(String name, StaffUpdateRequest request) {
        Role foundMember = memberRepository.findByName(name);

        if (foundMember == null) {
            return null;
        }

        Staff updatedStaff = new Staff(
                name,
                request.getMajor(),
                request.getGeneration(),
                request.getPart(),
                request.getPosition()
        );

        memberRepository.updateByName(name, updatedStaff);
        return updatedStaff;
    }

    public boolean deleteMember(String name) {
        return memberRepository.deleteByName(name);
    }
}