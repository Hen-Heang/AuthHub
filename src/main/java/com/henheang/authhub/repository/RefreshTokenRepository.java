// 2. Create a repository for managing refresh tokens
package com.henheang.authhub.repository;

import com.henheang.authhub.domain.RefreshToken;
import com.henheang.authhub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    List<RefreshToken> findAllByUser(User user);

//    @Modifying
//    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
//    void revokeAllUserTokens(User user);
//
//    @Modifying
//    @Query("DELETE FROM RefreshToken r WHERE r.expiryDate < :now")
//    void deleteAllExpiredTokens(Instant now);
}