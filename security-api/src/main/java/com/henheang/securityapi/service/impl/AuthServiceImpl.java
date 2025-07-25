package com.henheang.securityapi.service.impl;


import com.henheang.commonapi.components.common.api.ExitCode;
import com.henheang.securityapi.domain.AuthProvider;
import com.henheang.securityapi.domain.RefreshToken;
import com.henheang.securityapi.domain.Role;
import com.henheang.securityapi.domain.User;
import com.henheang.securityapi.exception.AuthException;
import com.henheang.securityapi.payload.AuthResponse;
import com.henheang.securityapi.payload.LoginRequest;
import com.henheang.securityapi.payload.SignUpRequest;
import com.henheang.securityapi.repository.UserRepository;
import com.henheang.securityapi.security.JwtTokenProvider;
import com.henheang.securityapi.service.AuthService;
import com.henheang.securityapi.service.RefreshTokenService;
import com.henheang.securityapi.service.RoleService;
import com.henheang.securityapi.service.UserService;
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
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Find the user
            User user = userRepository.findByEmail(loginRequest.getEmail())
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