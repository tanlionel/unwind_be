package com.capstone.unwind.controller;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.*;
import com.capstone.unwind.model.AuthDTO.*;
import com.capstone.unwind.model.TimeShareStaffDTO.LoginTSStaffRequestDto;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.TimeShareStaffService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class UserAuthController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final TimeShareStaffService timeShareStaffService;
    private static final String HEADER = "Authorization";
    private static final String SUB_STRING = "Bearer ";

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
    @GetMapping("/oauth2-success")
    public ResponseEntity<String> handleLoginGoogleSuccess(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null) {
            Cookie cookie = new Cookie("jwtToken", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không có sẵn");
        }
    }



    @PostMapping("/timeshare-company-staff/login")
    public ResponseEntity<?> signInStaffRole(@RequestBody LoginTSStaffRequestDto loginTSStaffRequestDto) throws AccountSuspendedException, OptionalNotFoundException, InvalidateException, TokenExpiredException, UserDoesNotExistException {
        TimeshareCompanyStaff timeshareCompanyStaff = timeShareStaffService.loginStaff(loginTSStaffRequestDto);
        String refreshToken = jwtService.generateRefreshTokenStaff(timeshareCompanyStaff);
        String accessToken = jwtService.generateAccessTokenStaff(refreshToken);

        return ResponseEntity.ok(LoginResponseStaffDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .resortId(timeshareCompanyStaff.getResort().getId())
                        .tsCompanyId(timeshareCompanyStaff.getTimeshareCompany().getId())
                        .build());
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) throws ErrMessageException {

            String token = userService.getTokenForgot(email);
            return ResponseEntity.ok("Token has been sent to your email.");

    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) throws ErrMessageException {

        userService.resetPassword(email, token, newPassword);

        return ResponseEntity.ok("Password reset successfully.");
    }
}
