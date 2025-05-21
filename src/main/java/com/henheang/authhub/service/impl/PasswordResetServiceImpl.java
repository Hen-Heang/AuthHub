package com.henheang.authhub.service.impl;

import com.henheang.authhub.common.api.ExitCode;
import com.henheang.authhub.domain.PasswordResetToken;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.AuthException;
import com.henheang.authhub.repository.PasswordResetTokenRepository;
import com.henheang.authhub.repository.UserRepository;
import com.henheang.authhub.service.EmailService;
import com.henheang.authhub.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.reset-password.token-expiration-minutes:1440}") // Default: 24 hours
    private int tokenExpirationMinutes;
    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;


    @Override
    public void createPasswordResetTokenEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);


        if (userOptional.isPresent()){
        logger.info("User found with email: {}", email);
        }else {
            logger.warn("No user found with email: {}", email);
            return;
        }

        User user = userOptional.get();

        passwordResetTokenRepository.deleteAll(passwordResetTokenRepository.findByUser(user));

//        Create new token
        String resetToken = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));

        passwordResetTokenRepository.save(passwordResetToken);

//        Construct reset password link
        String resetPasswordLink = String.format("%s/reset-password?token=%s", frontendUrl, resetToken);
        logger.info("Reset password link: {}", resetPasswordLink);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token).map(PasswordResetToken::isExpired).orElse(false);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(ExitCode.PASSWORD_RESET_TOKEN_INVALID));

        if (passwordResetToken.isExpired()) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new AuthException(ExitCode.PASSWORD_RESET_TOKEN_EXPIRED);
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

}
