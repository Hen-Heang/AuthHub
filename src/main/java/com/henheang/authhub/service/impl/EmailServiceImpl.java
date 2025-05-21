package com.henheang.authhub.service.impl;

import com.henheang.authhub.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Override
    public boolean sendPasswordResetEmail(String email, String name, String resetUrl) {

        log.info("Sending password reset email to {} with name {} and reset URL {}", email, name, resetUrl);
        return true;

    }
}
