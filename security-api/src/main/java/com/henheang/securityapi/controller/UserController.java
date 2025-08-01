package com.henheang.securityapi.controller;


import com.henheang.securityapi.payload.UpdateUserRequest;
import com.henheang.securityapi.service.UserService;
import jakarta.validation.Valid;
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
    public Object updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
            ){
        return ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(
            @PathVariable Long id){
        userService.deleteUser(id);
        return ok();
    }
}
