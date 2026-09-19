package com.likelion.pbl.week11.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class SignupRequest {

    private String name;
    private String major;
    private int generation;
    private String part;
    @NotBlank(message = "역할은 필수입니다.")
    private String roleName;
    private String studentId;
    private String position;
    private String password;

    public SignupRequest() {
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

    public String getPassword() {
        return password;
    }
}
