package com.likelion.likelionspring.auth.dto;

public class TokenResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}