package com.henheang.securityapi.service;


import com.henheang.securityapi.payload.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    Object signup(@Valid SignUpRequest signUpRequest);

    Object login(@Valid LoginRequest loginRequest);
}
