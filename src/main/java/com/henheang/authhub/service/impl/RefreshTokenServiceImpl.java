package com.henheang.authhub.service.impl;

import com.henheang.authhub.common.api.ExitCode;
import com.henheang.authhub.domain.RefreshToken;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.AuthException;
import com.henheang.authhub.repository.RefreshTokenRepository;
import com.henheang.authhub.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${jwt.refresh-token.expiration}")
    private String refreshTokenExpirationString;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // First, revoke all existing tokens for this user
        List<RefreshToken> existingTokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);
        for (RefreshToken token : existingTokens) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }

        // Create a new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        Duration duration = Duration.parse(refreshTokenExpirationString);
        refreshToken.setExpiryDate(Instant.now().plus(duration));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> !refreshToken.isRevoked())
                .filter(refreshToken -> refreshToken.getExpiryDate().isAfter(Instant.now()));
    }

    @Transactional
    public void revokeAllUserTokens(User user) {
        List<RefreshToken> validTokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);
        if (validTokens.isEmpty()) {
            return;
        }

        for (RefreshToken token : validTokens) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }



    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void deleteExpiredTokens() {
        Instant now = Instant.now();
        List<RefreshToken> expiredTokens = refreshTokenRepository.findAll().stream()
                .filter(token -> token.getExpiryDate().isBefore(now)).toList();
        refreshTokenRepository.deleteAll(expiredTokens);
    }


    @Override
    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    }


