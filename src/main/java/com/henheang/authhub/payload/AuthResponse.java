package com.henheang.authhub.payload;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType = "Bearer";

    private Long expiresIn;

    @Builder
    public AuthResponse(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;

    }

}
