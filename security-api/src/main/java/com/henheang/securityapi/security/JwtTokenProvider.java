// 5. Update the JwtTokenProvider to handle both access and refresh tokens
package com.henheang.securityapi.security;

import com.henheang.authhub.common.api.ExitCode;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.expiration}")
    private String jwtExpirationString;

    @Autowired
    private SecretKey jwtSecretKey;

    public String generateToken(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return generateToken(userPrincipal.getId(), null);
        } catch (Exception e) {
            throw new AuthException(ExitCode.JWT_CONFIGURATION_ERROR,
                    "Failed to generate JWT token: " + e.getMessage());
        }
    }

    public String generateToken(User user) {
        try {
            return generateToken(user.getId(), null);
        } catch (Exception e) {
            throw new AuthException(ExitCode.JWT_CONFIGURATION_ERROR,
                    "Failed to generate JWT token: " + e.getMessage());
        }
    }

    public String generateToken(Long userId, String tokenType) {
        try {
            Map<String, Object> claims = new HashMap<>();
            if (tokenType != null) {
                claims.put("token_type", tokenType);
            }

            Instant now = Instant.now();
            Duration duration = Duration.parse(jwtExpirationString);
            Instant expiryDate = now.plus(duration);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(Long.toString(userId))
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(expiryDate))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            throw new AuthException(ExitCode.JWT_CONFIGURATION_ERROR,
                    "Failed to generate JWT token: " + e.getMessage());
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new AuthException(ExitCode.TOKEN_INVALID,
                    "Failed to extract user ID from token: " + e.getMessage());
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return jwtSecretKey;
    }
}