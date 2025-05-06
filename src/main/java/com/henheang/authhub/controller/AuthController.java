package com.henheang.authhub.controller;

import com.henheang.authhub.common.api.ApiResponse;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.payload.AuthResponse;
import com.henheang.authhub.payload.LoginRequest;
import com.henheang.authhub.payload.SignUpRequest;
import com.henheang.authhub.payload.UserResponse;
import com.henheang.authhub.security.JwtTokenProvider;
import com.henheang.authhub.service.AuthService;
import com.henheang.authhub.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = authService.login(loginRequest);
        String token = jwtTokenProvider.generateToken(user);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = authService.signup(signUpRequest);
        String token = jwtTokenProvider.generateToken(user);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}