package com.henheang.authhub.service.impl;


import com.henheang.authhub.common.api.ExitCode;
import com.henheang.authhub.domain.AuthProvider;
import com.henheang.authhub.domain.RefreshToken;
import com.henheang.authhub.domain.Role;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.AuthException;
import com.henheang.authhub.payload.AuthResponse;
import com.henheang.authhub.payload.LoginRequest;
import com.henheang.authhub.payload.SignUpRequest;
import com.henheang.authhub.repository.UserRepository;
import com.henheang.authhub.security.JwtTokenProvider;
import com.henheang.authhub.service.AuthService;
import com.henheang.authhub.service.RefreshTokenService;
import com.henheang.authhub.service.RoleService;
import com.henheang.authhub.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.refresh-token.expiration}")
    private String refreshTokenExpirationString;

    @Override
    @Transactional
    public Object signup(SignUpRequest signUpRequest) {
        // Check if email already exists
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new AuthException(ExitCode.EMAIL_ALREADY_EXISTS);
        }

        // Create user
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setProvider(AuthProvider.LOCAL);
        user.setEmailVerified(true); // Set to true for testing, in production you would set this to false and implement email verification

        // Add Roles to new user
        Role role = roleService.getOrCreateRole("ROLE_USER");
        user.addRole(role);

        try {
            User savedUser = userService.saveUser(user);
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(savedUser);
            // Generate refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
            // Set expiration time

            Duration duration = Duration.parse(refreshTokenExpirationString);

            Long expirationTimeInSeconds = duration.getSeconds();
            return new AuthResponse(token, refreshToken.getToken(), expirationTimeInSeconds);

        } catch (Exception e) {
            throw new AuthException(ExitCode.REGISTRATION_FAILED,
                    "Registration failed: " + e.getMessage());
        }
    }

    @Override
    public Object login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getIdentifier(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Find the user
            User user = userRepository.findByEmail(loginRequest.getIdentifier())
                    .orElseThrow(() -> new AuthException(ExitCode.INVALID_CREDENTIALS));

            // Generate an access token
            String accessToken = jwtTokenProvider.generateToken(user);

            // Generate refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            // Calculate expiration time in seconds
            Duration duration = Duration.parse(refreshTokenExpirationString);
            long expiresInSeconds = duration.getSeconds();

            // Create an authentication response
            return new AuthResponse(accessToken, refreshToken.getToken(), expiresInSeconds);
        } catch (AuthenticationException e) {
            throw new AuthException(ExitCode.AUTHENTICATION_FAILED,
                    "Authentication failed: " + e.getMessage());
        }
    }

}