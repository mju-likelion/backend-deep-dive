package com.likelion.pbl.week11.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    public String getContent() {
        return content;
    }
}
