//package com.test.todoapi.config;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//@Configuration
//@Getter
//@Setter
//public class JwtConfig {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private String expirationTime;
//
//    @Bean
//    public SecretKey jwtSecretKey() {
//        try {
//            byte[] decodedKey = Base64.getDecoder().decode(secret);
//
//            if (decodedKey.length < 64) {
//                throw new IllegalArgumentException("JWT secret key must be at least 512 bits (64 bytes) for HS512");
//            }
//
//            return Keys.hmacShaKeyFor(decodedKey);
//        } catch (Exception e) {
//            System.err.println("Error with JWT secret: " + e.getMessage());
//            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
//            System.out.println("Generated JWT Secret: " + encodedKey);
//            return key;
//        }
//    }
//}