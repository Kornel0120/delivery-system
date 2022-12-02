package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, Long> {
}
