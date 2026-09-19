package com.BabyLion.Spring.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LionUpdateRequest {
    private String name;
    private String major;
    private int generation;
    private String part;
    private String studentId;
}
