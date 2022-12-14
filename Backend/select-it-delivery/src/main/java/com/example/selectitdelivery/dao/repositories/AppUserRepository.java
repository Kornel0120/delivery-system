package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Boolean existsByEmail(String email);

    Optional<AppUserEntity> findByEmail(String email);
}
