package com.henheang.authhub.controller;

import com.henheang.authhub.common.api.ApiResponse;
import com.henheang.authhub.common.api.ExitCode;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.AuthException;
import com.henheang.authhub.payload.*;
import com.henheang.authhub.security.JwtTokenProvider;
import com.henheang.authhub.service.AuthService;
import com.henheang.authhub.service.PasswordResetService;
import com.henheang.authhub.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration}")
    private String jwtExpirationString;

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginRequest loginRequest) {
//        User user = authService.login(loginRequest);
//        String token = jwtTokenProvider.generateToken(user);
        AuthResponse authResponse = (AuthResponse) authService.login(loginRequest);
        return ok(authResponse);
    }

    @PostMapping("/signup")
    public Object signup(@Valid @RequestBody SignUpRequest signUpRequest) {
//        User user = authService.signup(signUpRequest);
//        String token = jwtTokenProvider.generateToken(user);
        AuthResponse authResponse =(AuthResponse) authService.signup(signUpRequest);
        return ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.validateRefreshToken(request.getRefreshToken())
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    String accessToken = jwtTokenProvider.generateToken(user);

                    // Calculate expiration time in seconds
                    Duration duration = Duration.parse("PT" + jwtExpirationString.toUpperCase());
                    long expiresInSeconds = duration.getSeconds();
                    AuthResponse authResponse = new AuthResponse(
                            accessToken,
                            refreshToken.getToken(),
                            expiresInSeconds
                    );
                    return ResponseEntity.ok(ApiResponse.success(authResponse));
                })
                .orElseThrow(() -> new AuthException(ExitCode.TOKEN_EXPIRED, "Refresh token is expired. Please login again."));
    }

    @PostMapping("/logout")
    public Object logout(@Valid @RequestBody RefreshTokenRequest logoutRequest) {
        refreshTokenService.logout(logoutRequest.getRefreshToken());
        return ok();
    }


}