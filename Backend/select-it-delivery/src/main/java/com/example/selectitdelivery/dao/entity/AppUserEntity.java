package com.example.selectitdelivery.dao.entity;


import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class AppUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "role_user",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<RoleEntity> role = new HashSet<>();

    public AppUserEntity(String email, String password, Set<RoleEntity> role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
