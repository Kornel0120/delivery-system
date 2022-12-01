package com.example.selectitdelivery.dao.repositories;

import com.example.selectitdelivery.dao.compositeKeys.AppUserRoleCompositeKey;
import com.example.selectitdelivery.dao.entity.AppUserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRoleRepository extends JpaRepository<AppUserRoleEntity, AppUserRoleCompositeKey> {
}
