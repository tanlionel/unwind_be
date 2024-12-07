package com.capstone.unwind.config;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.enums.EmailEnum;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.model.AuthDTO.RegisterRequestDTO;
import com.capstone.unwind.model.EmailRequestDTO.EmailRequestDto;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceImplement.SendinblueService;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.capstone.unwind.config.EmailMessageConfig.REJECT_RENTAL_BOOKING_CONTENT;
import static com.capstone.unwind.config.EmailMessageConfig.REJECT_RENTAL_BOOKING_SUBJECT;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SendinblueService sendinblueService;


    public OAuth2LoginSuccessHandler(UserService userService, JwtService jwtService,UserRepository userRepository,SendinblueService sendinblueService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.sendinblueService = sendinblueService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2Token.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String sub = (String) attributes.get("sub");
        System.out.println(email);
        System.out.println(sub);


        User user = userRepository.findUserByEmail(email);
        //create new user
        if (user == null) {
            RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
            registerRequestDTO.setEmail(email);
            String password = RandomStringUtils.randomNumeric(6);
            registerRequestDTO.setPassword(password);
            registerRequestDTO.setUsername("gooleaccount"+RandomStringUtils.randomAlphabetic(9));
            registerRequestDTO.setRoleId(1);
            try {
                user = userService.registerUser(registerRequestDTO);
                try {
                    EmailRequestDto emailRequestDto = new EmailRequestDto();
                    emailRequestDto.setSubject("Tài khoản được tạo thành công");
                    emailRequestDto.setContent("Xin chào bạn, tài khoản bạn vưa được tạo thành công tại unwind với mật khẩu: "+ password+".Mời bạn vào hệ thống của chúng tôi để đổi mật khẩu mặc định.");
                    sendinblueService.sendEmailWithTemplate(
                            email,
                            EmailEnum.BASIC_MAIL,
                            emailRequestDto
                    );
                } catch (Exception e) {
                    throw new ErrMessageException("Failed to send email notification");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        String jwtToken = jwtService.generateRefreshToken(user);
        System.out.println(jwtToken);
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/api/auth/oauth2-success");
    }
}
