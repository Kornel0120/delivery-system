package com.example.selectitdelivery.dao.entity;

import com.example.selectitdelivery.dao.compositeKeys.RefreshTokenCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RefreshTokenCompositeKey.class)
@Entity(name = "refreshtoken")
public class RefreshToken {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private AppUserEntity user;

    @Id
    @Column(name = "refresh_token_value", nullable = false, unique = true)
    private String refreshTokenValue;

    @Column(name = "expiry_date",nullable = false)
    private Instant expiryDate;
}
