package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);

    UserResponse getCurrentUser();
}
