package com.likelion.likelionspring.auth.dto;

public class LoginRequest {
    private String name;
    private String password;

    public String getName() {return name;}
    public String getPassword() {return password;}

    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
}