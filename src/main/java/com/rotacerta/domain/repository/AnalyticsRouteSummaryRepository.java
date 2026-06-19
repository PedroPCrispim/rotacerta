package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.AnalyticsRouteSummary;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AnalyticsRouteSummaryRepository {
    AnalyticsRouteSummary save(AnalyticsRouteSummary summary);
    List<AnalyticsRouteSummary> findByTenantIdAndDateRange(UUID tenantId, LocalDateTime start, LocalDateTime end);
}
