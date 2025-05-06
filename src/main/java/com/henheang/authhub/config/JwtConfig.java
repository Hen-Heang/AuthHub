package com.henheang.authhub.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
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
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            // If key is too small for HS512, generate a secure key
            if (decodedKey.length * 8 < 512) {
                SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                this.secret = Base64.getEncoder().encodeToString(key.getEncoded());
                return key;
            }
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            // If there's any issue, generate a new secure key
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            this.secret = Base64.getEncoder().encodeToString(key.getEncoded());
            return key;
        }
    }
}