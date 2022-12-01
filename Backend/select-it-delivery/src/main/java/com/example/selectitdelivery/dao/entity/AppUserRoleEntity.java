package com.example.selectitdelivery.dao.entity;

import com.example.selectitdelivery.dao.compositeKeys.AppUserRoleCompositeKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AppUserRoleCompositeKey.class)
@Entity(name = "role_user")
public class AppUserRoleEntity {
    @Id
    @JoinColumn
    private long userId;
    @Id
    @JoinColumn
    private long roleId;
}
