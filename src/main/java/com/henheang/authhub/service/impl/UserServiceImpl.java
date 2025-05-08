package com.henheang.authhub.service.impl;

import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.ResourceNotFoundException;
import com.henheang.authhub.payload.UserResponse;
import com.henheang.authhub.repository.UserRepository;
import com.henheang.authhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public Object getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> new UserResponse
                        (user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getEmailVerified(),
                                user.getImageUrl(),
                                user.getProviderId()
                        ))

                .toList();
    }

    @Override
    public Object updateUser(String id) {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return null;
    }
}