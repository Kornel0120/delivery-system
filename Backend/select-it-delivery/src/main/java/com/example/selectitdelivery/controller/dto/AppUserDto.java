package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private long userId;
    private String email;
    private String password;
    private Set<RoleEntity> role = new HashSet<>();
}
