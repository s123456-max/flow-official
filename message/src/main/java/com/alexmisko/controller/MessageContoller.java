package com.alexmisko.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageContoller {
    @GetMapping("test")
    public String test(){
        return "hello";
    }
}
