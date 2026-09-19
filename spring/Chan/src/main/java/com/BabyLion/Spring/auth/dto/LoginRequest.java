package com.BabyLion.Spring.auth.dto;

import com.BabyLion.Spring.member.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String password;
    private String loginId;
}
