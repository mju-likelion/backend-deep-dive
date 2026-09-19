package com.likelion.pbl.week11.assignment.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignmentUpdateRequest {

    @NotBlank(message = "과제 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "과제 설명은 필수입니다.")
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
