package com.likelion.pbl.week11.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssignmentCreateWithMemberRequest {

    @NotNull(message = "멤버 ID는 필수입니다.")
    private Long memberId;

    @NotBlank(message = "과제 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "과제 설명은 필수입니다.")
    private String description;

    public Long getMemberId() {
        return memberId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public AssignmentCreateRequest toAssignmentCreateRequest() {
        return new AssignmentCreateRequest(title, description);
    }
}
