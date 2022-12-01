package com.example.selectitdelivery.dao.model;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
    private long roleId;
    private String roleName;
}
