package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity,Long> {
}
