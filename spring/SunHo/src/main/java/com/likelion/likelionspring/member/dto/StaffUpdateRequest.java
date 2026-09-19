package com.likelion.likelionspring.member.dto;

public class StaffUpdateRequest {
    private String major;
    private int generation;
    private String part;
    private String position;

    public String getMajor() {
        return major;
    }

    public int getGeneration() {
        return generation;
    }

    public String getPart() {
        return part;
    }

    public String getPosition() {
        return position;
    }
}
