package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.exception.InvalidateException;
import com.capstone.unwind.exception.TokenExpiredException;
import com.capstone.unwind.exception.UserDoesNotExistException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    Claims extractAllClaims(String token);
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(
            Map<String, Object> extraClaims,
            User user
    );
    String generateAccessToken(User user);
    String generateRefreshToken(User user);

    void detachToken(String token) throws UserDoesNotExistException, InvalidateException;
    boolean isValidToken(String token, UserDetails user);

    String generateAccessToken(String refreshToken) throws TokenExpiredException, UserDoesNotExistException, InvalidateException;

}