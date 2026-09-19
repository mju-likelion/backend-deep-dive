package com.BabyLion.Spring.auth.dto;

import com.BabyLion.Spring.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String name;
    private String major;
    private int generation;
    private String part;
    private String studentId;
    private String position;
    private String password;
    private String loginId;
}
