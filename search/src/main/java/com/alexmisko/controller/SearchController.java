package com.alexmisko.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @GetMapping("test")
    public String test(){
        return "test";
    }
}
