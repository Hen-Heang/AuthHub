package com.henheang.authhub.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {

    @NotBlank(message = "Email or phone number is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


}
