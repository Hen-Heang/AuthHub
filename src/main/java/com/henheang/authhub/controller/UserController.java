package com.henheang.authhub.controller;

import com.henheang.authhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class UserController extends BaseController{

    private final UserService userService;

    @GetMapping("/users")
    public Object getAllUsers() {
        return ok(userService.getAllUsers());
    }
}
