package com.likelion.likelionspring.global.exception;

public enum ErrorCodeEnum {
    FORBIDDEN(403, "본인의 게시물만 수정/삭제할 수 있습니다.");

    private final int status;
    private final String message;

    ErrorCodeEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}