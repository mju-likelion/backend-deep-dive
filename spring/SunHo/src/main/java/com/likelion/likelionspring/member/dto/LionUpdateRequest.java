package com.likelion.likelionspring.member.dto;

public class LionUpdateRequest {
    //name은 경로에서 받으므로 제외
    private String major;
    private int generation;
    private String part;
    private String studentId;

    public String getMajor() {
        return major;
    }

    public int getGeneration() {
        return generation;
    }

    public String getPart() {
        return part;
    }

    public String getStudentId() {
        return studentId;
    }
}
