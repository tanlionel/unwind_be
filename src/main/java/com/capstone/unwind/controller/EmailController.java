package com.capstone.unwind.controller;

import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.service.ServiceImplement.SendinblueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private  SendinblueService sendinblueService;


    @PostMapping()
    public String sendWelcomeEmail(@RequestParam String to, @RequestParam EmailEnum emailEnum, @RequestBody EmailRequestDto emailRequestDto) {
        sendinblueService.sendEmailWithTemplate(to, emailEnum ,emailRequestDto);
        return "Email sent successfully using template!";
    }
}