package com.example.selectitdelivery.security.services;

import com.example.selectitdelivery.dao.entity.RefreshToken;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.dao.repositories.RefreshTokenRepository;
import com.example.selectitdelivery.service.exceptions.RefreshTokenException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {

    @Value("${com.example.selectitdelivery.jwtExpirationMs}")
    private long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AppUserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshTokenValue(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs * 50L));
        refreshToken.setRefreshTokenValue(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getRefreshTokenValue(), "The refresh token expired on " + token.getExpiryDate().toString() +". Please make a new signin request");
        }

        return token;
    }

    public String findRefreshTokenByUserId(long appUserId) {
        log.error("findRefreshTokenByUserId: {}",appUserId);
        return refreshTokenRepository.findByUserUserId(appUserId).getRefreshTokenValue();
    }

    public boolean isUserHasARefreshToken(long appUserId) {
        if(refreshTokenRepository.findByUserUserId(appUserId) != null)
        {
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
