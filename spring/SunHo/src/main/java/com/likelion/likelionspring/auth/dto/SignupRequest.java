package com.likelion.likelionspring.auth.dto;

public class SignupRequest {
    private String name;
    private String password;
    private String major;
    private int generation;
    private String part;
    private String roleType;   // "LION" 또는 "STAFF"
    private String studentId;  // roleType이 LION일 때만 사용
    private String position;   // roleType이 STAFF일 때만 사용

    public String getName() {return name;}
    public String getPassword() {return password;}
    public String getMajor() {return major;}
    public int getGeneration() {return generation;}
    public String getPart() {return part;}
    public String getRoleType() {return roleType;}
    public String getStudentId() {return studentId;}
    public String getPosition() {return position;}

    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
    public void setMajor(String major) {this.major = major;}
    public void setGeneration(int generation) {this.generation = generation;}
    public void setPart(String part) {this.part = part;}
    public void setRoleType(String roleType) {this.roleType = roleType;}
    public void setStudentId(String studentId) {this.studentId = studentId;}
    public void setPosition(String position) {this.position = position;}
}