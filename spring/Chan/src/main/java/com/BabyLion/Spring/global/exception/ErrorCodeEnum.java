package com.BabyLion.Spring.global.exception;

public enum ErrorCodeEnum {
    INVALID_STUDENT_ID(400, "studentId는 숫자만 입력 가능합니다."),
    MEMBER_NOT_FOUND(404, "존재하지 않는 멤버입니다."),
    ASSIGNMENT_NOT_FOUND(404, "존재하지 않는 과제입니다."),
    DUPLICATE_MEMBER_NAME(409, "이미 존재하는 이름입니다."),
    EMPTY_NAME(400, "이름은 비어있을 수 없습니다."),
    INVALID_GENERATION(400, "0 이상의 기수를 입력해주십시오."),
    INVALID_PASSWORD(401, "비밀번호가 틀렸습니다"),
    MEMBER_FORBIDDEN(403, "본인의 정보만 수정/삭제할 수 있습니다."),
    ASSIGNMENT_FORBIDDEN(403, "본인의 과제만 수정/삭제할 수 있습니다."),
    COMMENT_FORBIDDEN(403, "본인의 댓글만 수정/삭제할 수 있습니다."),
    COMMENT_NOT_FOUND(404, "존재하지 않는 댓글입니다."),
    LOGIN_FAILED(401, "아이디 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_LOGIN_ID(409, "이미 사용 중인 아이디입니다.");

    private final int status;
    private final String message;

    ErrorCodeEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
}

