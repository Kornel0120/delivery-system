package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Boolean existsByEmail(String email);
}
