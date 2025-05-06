package com.henheang.authhub.controller;


import com.henheang.authhub.domain.User;
import com.henheang.authhub.payload.LoginRequest;
import com.henheang.authhub.payload.SignUpRequest;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthController extends BaseController{

    private AuthService authService;
    private PasswordResetService passwordResetService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        User user = authService.login(loginRequest);
        return ok(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        User user = authService.signup(signUpRequest);
        return ok(user);
    }



}
