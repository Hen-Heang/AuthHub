package com.henheang.authhub.repository;

import com.henheang.authhub.domain.PasswordResetToken;
import com.henheang.authhub.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

}
