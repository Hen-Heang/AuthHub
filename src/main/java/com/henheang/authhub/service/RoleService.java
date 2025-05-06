package com.henheang.authhub.service;

import com.henheang.authhub.domain.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    Role getOrCreateRole(String roleName);
}
