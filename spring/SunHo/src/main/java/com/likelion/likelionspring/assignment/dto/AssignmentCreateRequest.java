package com.likelion.likelionspring.assignment.dto;

public class AssignmentCreateRequest {
    private String title;
    private String description;


    public AssignmentCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
