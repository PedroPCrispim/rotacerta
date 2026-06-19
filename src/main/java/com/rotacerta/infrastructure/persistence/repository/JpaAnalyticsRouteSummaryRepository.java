package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.AnalyticsRouteSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface JpaAnalyticsRouteSummaryRepository extends JpaRepository<AnalyticsRouteSummaryEntity, UUID> {
    List<AnalyticsRouteSummaryEntity> findByTenantIdAndCreatedAtBetween(UUID tenantId, LocalDateTime start, LocalDateTime end);
}
