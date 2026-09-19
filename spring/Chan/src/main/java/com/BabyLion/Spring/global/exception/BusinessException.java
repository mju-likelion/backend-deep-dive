package com.BabyLion.Spring.global.exception;

public class BusinessException extends RuntimeException {
    private final ErrorCodeEnum errorCode;

    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}