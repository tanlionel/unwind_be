package com.capstone.unwind.config;

import com.capstone.unwind.entity.TimeshareCompanyStaff;
import com.capstone.unwind.exception.InvalidateException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import com.capstone.unwind.repository.TimeshareCompanyStaffRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.JwtService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final TimeshareCompanyStaffRepository timeshareCompanyStaffRepository;
    private final UserRepository userRepository;
    private static final String HEADER = "Authorization";
    private static final String SUB_STRING = "Bearer ";
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER);
        String jwt;
        String userName;

        if (authHeader == null || !authHeader.startsWith(SUB_STRING)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(SUB_STRING.length());
        userName = jwtService.extractUsername(jwt);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = null;
            try {
                user = userRepository.findUserByUserName(userName);
                if (user==null) {
                    Integer tsId = jwtService.extractTsId(jwt);
                    user = timeshareCompanyStaffRepository.findTimeshareCompanyStaffByUserNameAndTimeshareCompanyId(userName,tsId);
                    if (user==null) throw new RuntimeException();
                }
            } catch ( Exception e) {
                throw new RuntimeException(e);
            }
            if (jwtService.isValidToken(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, jwt, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
