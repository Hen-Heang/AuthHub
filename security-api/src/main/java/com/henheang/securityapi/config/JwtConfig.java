package com.henheang.securityapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@Getter
@Setter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    @Bean
    public SecretKey jwtSecretKey() {
        try {
            // Use the predefined strong secret
            String strongSecret = "dGhpc0lzQVNlY3VyZUFuZFN0cm9uZ0p3dFNlY3JldEtleUZvckF1dGhIdWJBcHBsaWNhdGlvbjIwMjU=";
            byte[] decodedKey = Base64.getDecoder().decode(strongSecret);
            return new SecretKeySpec(decodedKey, "HmacSHA512");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JWT secret key", e);
        }
    }
}