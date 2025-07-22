package com.henheang.commonapi.components.security;

import com.kosign.wepoint.common.payload.security.ClientAuthRequest;
import com.kosign.wepoint.common.payload.security.ClientTokenRequest;
import com.kosign.wepoint.common.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtConfig;

    public long getExpireIn() {
        return jwtConfig.expiration().getSeconds();
    }

    public  String doGenerateToken(ClientAuthRequest request) {

        Instant instant = Instant.now();
        Map<String, Object> claim = new HashMap<>();
        claim.put("cus_id",request.getUserId());
        claim.put("client_id",request.getClientId());
        claim.put("cus_phone",request.getCustomerPhone());
        claim.put("cus_name",request.getCustomerName());
        long twentyFourHoursInSeconds = 24 * 60 * 60;

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(twentyFourHoursInSeconds, ChronoUnit.SECONDS))
                .claims(c -> c.putAll(claim))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }


    public  String doGenerateToken(ClientTokenRequest request) {

        Instant instant = Instant.now();
        Map<String, Object> claim = new HashMap<>();
        claim.put("client_secret",request.clientSecret());
        claim.put("client_id",request.clientId());
        long twentyFourHoursInSeconds = 24 * 60 * 60;

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(twentyFourHoursInSeconds, ChronoUnit.SECONDS))
                .claims(c -> c.putAll(claim))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}

