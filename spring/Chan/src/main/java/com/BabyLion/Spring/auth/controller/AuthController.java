package com.BabyLion.Spring.auth.controller;

import com.BabyLion.Spring.auth.dto.LoginRequest;
import com.BabyLion.Spring.auth.dto.SignupRequest;
import com.BabyLion.Spring.auth.dto.TokenResponse;
import com.BabyLion.Spring.auth.service.AuthService;
import com.BabyLion.Spring.member.domain.Member;
import com.BabyLion.Spring.member.dto.LionCreateRequest;
import com.BabyLion.Spring.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Signup/Login Test")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "회원가입", description = "회원가입 테스트")
    @PostMapping("/auth/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody SignupRequest dto) {
        Member member = authService.signup(dto);
        MemberResponse response = MemberResponse.from(member);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "로그인", description = "로그인 테스트")
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest dto){
        String token = authService.login(dto);
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
