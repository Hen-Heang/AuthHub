package com.henheang.securityapi.service.impl;

import com.henheang.authhub.domain.RefreshToken;
import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.ResourceNotFoundException;
import com.henheang.authhub.payload.UpdateUserRequest;
import com.henheang.authhub.payload.UserResponse;
import com.henheang.authhub.repository.RefreshTokenRepository;
import com.henheang.authhub.repository.UserRepository;
import com.henheang.securityapi.service.UserService;
import com.henheang.authhub.utils.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        String cleanPhone = phoneNumber.trim();

        // Check if exists with the provided format
        if (userRepository.existsByPhoneNumber((cleanPhone))){
            return true;
        }

        // Check with normalized format
        String normalizedPhone = PhoneNumberUtil.normalizePhoneNumber(cleanPhone);
        return !normalizedPhone.equals(cleanPhone) && userRepository.existsByPhoneNumber(normalizedPhone);
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
                                user.getPhoneNumber(),
                                user.getEmailVerified(),
                                user.getImageUrl(),
                                user.getProviderId()
                        ))

                .toList();
    }

    @Override
    public Object updateUser(Long id, UpdateUserRequest updateUserRequest) {
//        Find user for update
        User user = userRepository.findById(id).orElseThrow(()
        -> new ResourceNotFoundException("User", "id", id));

//        Check if name is already in use
        if (updateUserRequest.getName() != null){
            Optional<User> existingUser = userRepository.findByName(updateUserRequest.getName());

            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                throw new RuntimeException("Name is already in use by another account");
            }
            user.setName(updateUserRequest.getName());
        }
//        Check if email is already in use
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().equals(user.getEmail()) ){
            Optional<User> existingUser = userRepository.findByEmail(updateUserRequest.getEmail());

            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
             throw new RuntimeException("Email is already in use by another account");
            }
            user.setEmail(updateUserRequest.getEmail());

        }
        if (updateUserRequest.getImageUrl() != null){
            user.setImageUrl(updateUserRequest.getImageUrl());
        }
        if (updateUserRequest.getEmailVerified() != null){
            user.setEmailVerified(updateUserRequest.getEmailVerified());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
//        Get user for delete
        User user = getUserById(id);
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUser(user);
        refreshTokenRepository.deleteAll(refreshTokens);

        refreshTokenRepository.flush();
        userRepository.delete(user);
    }
}
