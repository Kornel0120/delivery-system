package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.ShipmentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatusEntity,Long> {

}
