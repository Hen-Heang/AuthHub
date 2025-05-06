package com.henheang.authhub.service;


import com.henheang.authhub.domain.User;
import com.henheang.authhub.payload.LoginRequest;
import com.henheang.authhub.payload.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    User signup(@Valid SignUpRequest signUpRequest);

    User login(@Valid LoginRequest loginRequest);
}
