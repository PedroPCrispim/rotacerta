package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.AnalyticsDailyAggregator;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface AnalyticsDailyAggregatorRepository {
    AnalyticsDailyAggregator save(AnalyticsDailyAggregator aggregator);
    Optional<AnalyticsDailyAggregator> findByTenantIdAndDate(UUID tenantId, LocalDate date);
    List<AnalyticsDailyAggregator> findByTenantIdAndDateRange(UUID tenantId, LocalDate start, LocalDate end);
}
