package com.example.selectitdelivery.dao.compositeKeys;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AppUserRoleCompositeKey implements Serializable {
    private long userId;
    private long roleId;
}
