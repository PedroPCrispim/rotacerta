package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.AnalyticsDailyAggregatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface JpaAnalyticsDailyAggregatorRepository extends JpaRepository<AnalyticsDailyAggregatorEntity, UUID> {
    Optional<AnalyticsDailyAggregatorEntity> findByTenantIdAndReferenceDate(UUID tenantId, LocalDate date);
    List<AnalyticsDailyAggregatorEntity> findByTenantIdAndReferenceDateBetween(UUID tenantId, LocalDate start, LocalDate end);
}
