package com.henheang.authhub.service;

import com.henheang.authhub.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email);
    User saveUser(User user);
}
