package com.capstone.unwind.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin
public class TestController {
    @GetMapping("unsecured")
    private String getFromUnsecured(){
        return "hello from un-secured";
    }
    @GetMapping("secured")
    private String getFromSecured(){
        return "hello from secured";
    }
}
