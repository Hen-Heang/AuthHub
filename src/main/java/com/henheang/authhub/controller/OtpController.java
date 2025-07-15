package com.henheang.authhub.controller;


import com.henheang.authhub.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1/otp")
@RequiredArgsConstructor

public class OtpController extends BaseController {

    private final OtpService otpService;

    @PostMapping("/send")
    public Object sendOtp() {

        return null;
    }

}
