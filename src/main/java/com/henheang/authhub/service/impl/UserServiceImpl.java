package com.henheang.authhub.service.impl;

import com.henheang.authhub.domain.User;
import com.henheang.authhub.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
