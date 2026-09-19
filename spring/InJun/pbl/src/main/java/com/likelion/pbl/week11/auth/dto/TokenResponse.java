package com.likelion.pbl.week11.auth.dto;

public class TokenResponse {

    private final String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
