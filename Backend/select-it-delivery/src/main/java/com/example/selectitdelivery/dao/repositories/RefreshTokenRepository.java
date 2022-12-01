package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.compositeKeys.RefreshTokenCompositeKey;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, RefreshTokenCompositeKey> {
    Optional<RefreshToken> findByRefreshTokenValue(String refreshTokenValue);

    @Modifying
    int deleteByUser(AppUserEntity user);

    RefreshToken findByUserUserId(long userId);
}
