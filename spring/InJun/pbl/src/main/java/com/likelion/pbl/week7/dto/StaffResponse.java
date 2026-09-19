package com.likelion.pbl.week7.dto;

import com.likelion.pbl.week7.domain.role.Staff;

public class StaffResponse {

    private String name;
    private String major;
    private int generation;
    private String part;
    private String roleName;
    private String position;

    public StaffResponse(String name, String major, int generation, String part, String roleName, String position) {
        this.name = name;
        this.major = major;
        this.generation = generation;
        this.part = part;
        this.roleName = roleName;
        this.position = position;
    }

    public static StaffResponse from(Staff staff) {
        return new StaffResponse(
                staff.getName(),
                staff.getMajor(),
                staff.getGeneration(),
                staff.getPart(),
                staff.roleName(),
                staff.getPosition()
        );
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

    public String getPosition() {
        return position;
    }


}
