package com.henheang.authhub.controller;

import com.henheang.authhub.payload.UpdateUserRequest;
import com.henheang.authhub.service.UserService;
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

//            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
//        check if user is updating their own profile or has admin role
//        if (userPrincipal.getAuthorities().stream().noneMatch(
//                        a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//            return ResponseEntity.status(403).body("You can only update your own profile");
//        }

        return ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public Object deleteUser(
            @PathVariable Long id){
        userService.deleteUser(id);
        return ok();
    }
}
