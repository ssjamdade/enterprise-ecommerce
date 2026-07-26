package com.ecommerce.service.impl;

import com.ecommerce.auth.dto.AuthResponse;
import com.ecommerce.auth.dto.LoginRequest;
import com.ecommerce.auth.dto.RefreshTokenRequest;
import com.ecommerce.auth.dto.RegisterRequest;
import com.ecommerce.auth.entity.RefreshTokenEntity;
import com.ecommerce.auth.entity.RoleEntity;
import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.auth.repository.RefreshTokenRepo;
import com.ecommerce.auth.repository.RoleRepo;
import com.ecommerce.auth.repository.UserRepo;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.security.jwt.JwtService;
import com.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final RefreshTokenRepo refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("Email already registered.");
        }

        // Get default role
        RoleEntity userRole = roleRepository.findByName(RoleEntity.Role.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found."));

        // Create user
        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(jwtService.extractExpiration(refreshToken))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        // Return response
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(jwtService.extractExpiration(refreshToken))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {

    }
}
