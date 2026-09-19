package com.likelion.likelionspring.comment.dto;

public class CommentCreateRequest {

    private String content;

    public CommentCreateRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}