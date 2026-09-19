package com.likelion.pbl.week11.auth.controller;

import com.likelion.pbl.week11.auth.dto.LoginRequest;
import com.likelion.pbl.week11.auth.dto.SignupRequest;
import com.likelion.pbl.week11.auth.dto.TokenResponse;
import com.likelion.pbl.week11.auth.service.AuthService;
import com.likelion.pbl.week11.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
