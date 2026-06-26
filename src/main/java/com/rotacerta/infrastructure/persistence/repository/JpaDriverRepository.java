package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaDriverRepository extends JpaRepository<DriverEntity, UUID> {
    List<DriverEntity> findAllByCompanyId(UUID companyId);
}
