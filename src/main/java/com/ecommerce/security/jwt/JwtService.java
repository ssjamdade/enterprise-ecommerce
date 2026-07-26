package com.ecommerce.security.jwt;

import com.ecommerce.auth.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface JwtService {

    String generateAccessToken(UserEntity user);

    String generateRefreshToken(UserEntity user);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    LocalDateTime extractExpiration(String refreshToken);
}
