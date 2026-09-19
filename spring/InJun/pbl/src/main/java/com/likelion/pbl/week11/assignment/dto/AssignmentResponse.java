package com.likelion.pbl.week11.assignment.dto;

import com.likelion.pbl.week11.assignment.domain.Assignment;

public class AssignmentResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final Long memberId;
    private final String memberName;

    public AssignmentResponse(Long id, String title, String description, Long memberId, String memberName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public static AssignmentResponse from(Assignment assignment) {
        return new AssignmentResponse(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getMember().getId(),
                assignment.getMember().getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }
}
