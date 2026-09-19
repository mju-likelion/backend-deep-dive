package com.likelion.likelionspring.global.exception;

public class InvalidMemberRequestException extends RuntimeException {
    public InvalidMemberRequestException(String message) {
        super(message);
    }
}