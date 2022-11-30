package com.example.selectitdelivery.dao.model;

import com.example.selectitdelivery.dao.entity.RoleEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUser {
    private long userId;
    private String email;
    private String password;
    private Set<RoleEntity> role = new HashSet<>();
}
