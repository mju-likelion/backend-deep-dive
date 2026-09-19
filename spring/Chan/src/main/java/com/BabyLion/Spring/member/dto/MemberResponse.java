package com.BabyLion.Spring.member.dto;

import com.BabyLion.Spring.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String name;
    private String major;
    private int generation;
    private String part;
    private String roleName;
    private String studentId;
    private String position;

    public static MemberResponse from(Member member) {
        MemberResponse dto = new MemberResponse();
        dto.id = member.getId();
        dto.name = member.getName();
        dto.major = member.getMajor();
        dto.generation = member.getGeneration();
        dto.part = member.getPart();
        dto.roleName = member.getRoleType().getDisplayName();
        dto.studentId = member.getStudentId();
        dto.position = member.getPosition();
        return dto;
    }
}
