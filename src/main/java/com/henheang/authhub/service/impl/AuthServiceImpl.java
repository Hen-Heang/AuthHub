package com.henheang.authhub.service.impl;

import com.henheang.authhub.domain.AuthProvider;
import com.henheang.authhub.domain.Role;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.BadRequestException;
import com.henheang.authhub.payload.LoginRequest;
import com.henheang.authhub.payload.SignUpRequest;
import com.henheang.authhub.security.JwtTokenProvider;
import com.henheang.authhub.service.AuthService;
import com.henheang.authhub.service.RoleService;
import com.henheang.authhub.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public User signup(SignUpRequest signUpRequest) {

//        Check if email already exists
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

//        create user
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setProvider(AuthProvider.LOCAL);
        user.setEmailVerified(false);

//        Add Roles to new user
        Role role = roleService.getOrCreateRole("ROLE_USER");
        user.addRole(role);
        return userService.saveUser(user);

    }

    @Override
    public User login(LoginRequest loginRequest) {

        return null;
    }
}
