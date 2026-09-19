package com.BabyLion.Spring.global.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status; // http 상태 코드
    private String message; // 에러 메세지

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
