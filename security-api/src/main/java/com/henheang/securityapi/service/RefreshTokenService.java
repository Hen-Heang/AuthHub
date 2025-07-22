package com.henheang.securityapi.service;

import com.henheang.securityapi.domain.RefreshToken;
import com.henheang.securityapi.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> validateRefreshToken(String token);

    void logout(@NotBlank(message = "Refresh token is required") String refreshToken);
}
