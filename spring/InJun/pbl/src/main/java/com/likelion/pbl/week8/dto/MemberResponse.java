package com.likelion.pbl.week8.dto;

import com.likelion.pbl.week8.domain.Member;

public class MemberResponse {

    private Long id;
    private String name;
    private String major;
    private int generation;
    private String part;
    private String roleName;
    private String studentId;
    private String position;

    public MemberResponse(Long id, String name, String major, int generation,
                          String part, String roleName, String studentId, String position) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.generation = generation;
        this.part = part;
        this.roleName = roleName;
        this.studentId = studentId;
        this.position = position;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getMajor(),
                member.getGeneration(),
                member.getPart(),
                member.getRoleType().getDisplayName(),
                member.getStudentId(),
                member.getPosition()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public int getGeneration() {
        return generation;
    }

    public String getPart() {
        return part;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPosition() {
        return position;
    }
}