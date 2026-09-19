package com.BabyLion.Spring.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello, Likelion!";
    }

    // 보너스 과제
    @GetMapping("/hello/{name}")
    public String bonus(@PathVariable String name){
        return "Hello, " + name + "!";
    }

}
