package com.henheang.authhub.config;

import com.henheang.authhub.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer  implements CommandLineRunner {

    private final RoleService roleService;
    @Override
    public void run(String... args) {
        // Initialize roles
        roleService.getOrCreateRole("ROLE_USER");
        roleService.getOrCreateRole("ROLE_ADMIN");
    }

}
