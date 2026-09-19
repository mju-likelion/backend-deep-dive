package com.BabyLion.Spring.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignmentUpdateRequest {
    private String title;
    private String description;
}
