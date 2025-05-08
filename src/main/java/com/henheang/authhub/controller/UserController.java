package com.henheang.authhub.controller;

import com.henheang.authhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController extends BaseController{

    private final UserService userService;

    @GetMapping
    public Object getAllUsers() {
        return ok(userService.getAllUsers());
    }

    @PatchMapping("/{id}")
    public Object updateUser(@PathVariable String id){
        return ok(userService.updateUser(id));
    }
}
