package com.henheang.authhub.security;

import com.henheang.authhub.domain.User;
import com.henheang.authhub.exception.ResourceNotFoundException;
import com.henheang.authhub.repository.UserRepository;
import com.henheang.authhub.utils.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }

    /*
    Find the user by email or phone number.
     */

    @Transactional(readOnly = true)
    public java.util.Optional<User> findUserByIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return java.util.Optional.empty();
        }

        String cleanIdentifier = identifier.trim();

        // First try to find by exact match (email or phone)
        java.util.Optional<User> user = userRepository.findByEmailOrPhoneNumber(cleanIdentifier);

        if (user.isPresent()) {
            return user;
        }

        // If it looks like a phone number, try normalizing it
        if (PhoneNumberUtil.isPhoneNumber(cleanIdentifier)) {
            String normalizedPhone = PhoneNumberUtil.normalizePhoneNumber(cleanIdentifier);
            if (!normalizedPhone.equals(cleanIdentifier)) {
                return userRepository.findByPhoneNumber(normalizedPhone);
            }
        }

        return java.util.Optional.empty();
    }


}