package com.henheang.authhub.config;


import com.henheang.authhub.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor


public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * Clean up expired password reset tokens daily at midnight
     */

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupExpiredPasswordResetTokens() {
        logger.info("Running scheduled cleanup of expired password reset tokens");
        passwordResetTokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
    }
}
