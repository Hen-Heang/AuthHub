package com.henheang.authhub.service.impl;

import com.henheang.authhub.domain.RefreshToken;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.repository.RefreshTokenRepository;
import com.henheang.authhub.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.util.Locale.filter;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${jwt.refresh-token.expiration}")
    private String refreshTokenExpirationString;

    @Override
    public RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.revokeAllUserTokens(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setRevoked(false);
        Duration duration = Duration.parse("PT" + refreshTokenExpirationString.toUpperCase());
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
        refreshTokenRepository.revokeAllUserTokens(user);
    }


    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

    @Override
    public void logout(String refreshToken) {

    }

}
