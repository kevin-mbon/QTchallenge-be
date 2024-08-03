package com.QTchallenge.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class Hello {
    @GetMapping("/hello")
    public String hello() {
        System.getenv();
        return "Spring boot is up and running";
    }


}
