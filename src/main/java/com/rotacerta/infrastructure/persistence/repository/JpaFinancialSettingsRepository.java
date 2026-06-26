package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.FinancialSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaFinancialSettingsRepository extends JpaRepository<FinancialSettingsEntity, UUID> {
    Optional<FinancialSettingsEntity> findByCompanyId(UUID companyId);
}
