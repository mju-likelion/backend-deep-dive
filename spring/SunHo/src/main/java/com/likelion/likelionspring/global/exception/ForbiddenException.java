package com.likelion.likelionspring.global.exception;

public class ForbiddenException extends RuntimeException {

    private final ErrorCodeEnum errorCode;

    public ForbiddenException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}