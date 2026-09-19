package com.likelion.pbl.week8.domain;

public enum RoleType {
    LION("아기사자"),
    STAFF("운영진");

    private final String displayName;

    RoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}