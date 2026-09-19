package com.likelion.likelionspring.global.exception;

public class DuplicateMemberNameException extends RuntimeException {
    public DuplicateMemberNameException(String message) {
        super(message);
    }
}