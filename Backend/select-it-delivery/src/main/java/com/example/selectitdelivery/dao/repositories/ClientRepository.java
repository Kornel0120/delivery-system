package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
}
