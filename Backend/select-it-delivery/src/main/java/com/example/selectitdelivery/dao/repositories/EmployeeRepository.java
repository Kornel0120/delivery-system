package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}
