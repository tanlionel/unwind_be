package com.capstone.unwind.controller;

import com.capstone.unwind.config.JwtAuthenticationToken;
import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.model.TimeShareStaffDTO.LoginTSStaffRequestDto;
import com.capstone.unwind.model.TimeShareStaffDTO.TimeShareCompanyStaffDTO;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin
public class TestController {
    @Autowired
    private final TimeShareStaffService timeShareStaffService;
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    @Autowired
    private final UserService userService;

    @GetMapping("secured")
    private String getFromSecured(){
        return "hello from secured";
    }
    @GetMapping("tsStaff")
    private String  getFromStaff(){
        return "siuu";
    }
    @GetMapping("/api/some-endpoint")
    public String someEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            String jwt = jwtAuth.getToken(); // Lấy JWT từ token
            return "Token: " + jwt;
        }
        return "No token available";
    }
    @GetMapping("cdm")
    public TimeShareCompanyStaffDTO timeshareCompanyStaff(){
        return timeShareStaffService.getLoginStaff();
    }
}
