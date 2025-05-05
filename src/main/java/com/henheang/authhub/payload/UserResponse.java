package com.henheang.authhub.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long Id;

    private String name;

    private String email;

    private Boolean emailVerified;

    private String imageUrl;

    private String provider;



}
