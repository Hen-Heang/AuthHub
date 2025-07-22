package com.henheang.securityapi.controller;
import com.henheang.commonapi.components.common.api.ApiResponse;
import com.henheang.commonapi.components.common.api.ExitCode;
import com.henheang.securityapi.domain.User;
import com.henheang.securityapi.exception.AuthException;
import com.henheang.securityapi.payload.*;
import com.henheang.securityapi.security.JwtTokenProvider;
import com.henheang.securityapi.security.UserPrincipal;
import com.henheang.securityapi.service.AuthService;
import com.henheang.securityapi.service.PasswordResetService;
import com.henheang.securityapi.service.RefreshTokenService;
import com.henheang.securityapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    @Value("${jwt.expiration}")
    private String jwtExpirationString;

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = (AuthResponse) authService.login(loginRequest);
        return ok(authResponse);
    }

    @PostMapping("/signup")
    public Object signup(@Valid @RequestBody SignUpRequest signUpRequest) {
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

    @GetMapping("/user")
    public Object getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userService.getUserById(userPrincipal.getId());
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getEmailVerified(),
                user.getImageUrl(),
                user.getProvider() != null ? user.getProvider().toString() : "LOCAL"
        );
        return ok(userResponse);
    }


    //    Forgot password
    @PostMapping("/forgot-password")
    public Object forgotPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetService.createPasswordResetTokenEmail(passwordResetRequest.getEmail());
        return ok();
    }

    @GetMapping("/reset-password")
    public Object resetPassword(@RequestParam("token") String token) {
       boolean isValidToken = passwordResetService.validatePasswordResetToken(token);
        return ok(new PasswordResetResponse(isValidToken));
    }

    @PostMapping("/reset-password")
    public Object resetPassword(@Valid @RequestBody NewPasswordRequest resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
        return ok();
    }
}