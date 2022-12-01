package com.example.selectitdelivery.dao.compositeKeys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RefreshTokenCompositeKey implements Serializable {
    private long user;
    private String refreshTokenValue;
}
