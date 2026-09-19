package com.likelion.likelionspring.auth.controller;

import com.likelion.likelionspring.auth.dto.LoginRequest;
import com.likelion.likelionspring.auth.dto.SignupRequest;
import com.likelion.likelionspring.auth.dto.TokenResponse;
import com.likelion.likelionspring.auth.service.AuthService;
import com.likelion.likelionspring.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}