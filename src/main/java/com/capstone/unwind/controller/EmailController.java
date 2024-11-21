package com.capstone.unwind.controller;

import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.service.ServiceImplement.SendinblueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private  SendinblueService sendinblueService;


    @PostMapping()
    public String sendWelcomeEmail(@RequestParam String to, @RequestParam EmailEnum emailEnum, @RequestParam String name,@RequestParam String contentHeader,@RequestParam String subject) {
        sendinblueService.sendEmailWithTemplate(to, emailEnum ,name,contentHeader,subject);
        return "Email sent successfully using template!";
    }
}