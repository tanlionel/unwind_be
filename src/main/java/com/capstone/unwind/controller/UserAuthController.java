package com.capstone.unwind.controller;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.model.AuthDTO.BasicUserResponseDTO;
import com.capstone.unwind.model.AuthDTO.LoginRequestDTO;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.AuthDTO.ResponseObjectDTO;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserAuthController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registeredUser) throws Exception{
        User user = userService.registerUser(registeredUser);
        return ResponseEntity.status(HttpStatus.OK).body(BasicUserResponseDTO.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody LoginRequestDTO loginUser) throws Exception {

        User user = userService.login(loginUser.getEmail(), loginUser.getPassword());


        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(refreshToken);

        return ResponseEntity.ok(
                ResponseObjectDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

}
