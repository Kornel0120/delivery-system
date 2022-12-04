package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> readByUserUserId(long userId);
}
