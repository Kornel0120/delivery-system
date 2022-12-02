package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentStatusCatalogRepository extends JpaRepository<ShipmentStatusCatalogEntity,Long> {
    Optional<ShipmentStatusCatalogEntity> findByStatusName(String statusName);
}
