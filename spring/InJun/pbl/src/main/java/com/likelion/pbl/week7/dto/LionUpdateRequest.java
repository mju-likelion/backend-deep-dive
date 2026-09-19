package com.likelion.pbl.week7.dto;

public class LionUpdateRequest {
    private String major;
    private int generation;
    private String part;
    private String studentId;

    public LionUpdateRequest() {
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

    public String getStudentId() {
        return studentId;
    }
}
