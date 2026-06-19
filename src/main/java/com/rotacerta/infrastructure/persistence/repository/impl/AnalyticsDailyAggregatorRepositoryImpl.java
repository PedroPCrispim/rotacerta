package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.AnalyticsDailyAggregator;
import com.rotacerta.domain.repository.AnalyticsDailyAggregatorRepository;
import com.rotacerta.infrastructure.persistence.entity.AnalyticsDailyAggregatorEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaAnalyticsDailyAggregatorRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnalyticsDailyAggregatorRepositoryImpl implements AnalyticsDailyAggregatorRepository {

    private final JpaAnalyticsDailyAggregatorRepository jpaRepository;

    public AnalyticsDailyAggregatorRepositoryImpl(JpaAnalyticsDailyAggregatorRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AnalyticsDailyAggregator save(AnalyticsDailyAggregator aggregator) {
        AnalyticsDailyAggregatorEntity entity = AnalyticsDailyAggregatorEntity.builder()
                .id(aggregator.getId())
                .tenantId(aggregator.getTenantId())
                .referenceDate(aggregator.getReferenceDate())
                .kmSaved(aggregator.getKmSaved())
                .timeSaved(aggregator.getTimeSaved())
                .costSaved(aggregator.getCostSaved())
                .deliveriesCount(aggregator.getDeliveriesCount())
                .activeVehiclesCount(aggregator.getActiveVehiclesCount())
                .build();
        entity = jpaRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<AnalyticsDailyAggregator> findByTenantIdAndDate(UUID tenantId, LocalDate date) {
        return jpaRepository.findByTenantIdAndReferenceDate(tenantId, date).map(this::mapToDomain);
    }

    @Override
    public List<AnalyticsDailyAggregator> findByTenantIdAndDateRange(UUID tenantId, LocalDate start, LocalDate end) {
        return jpaRepository.findByTenantIdAndReferenceDateBetween(tenantId, start, end).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private AnalyticsDailyAggregator mapToDomain(AnalyticsDailyAggregatorEntity entity) {
        return AnalyticsDailyAggregator.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .referenceDate(entity.getReferenceDate())
                .kmSaved(entity.getKmSaved())
                .timeSaved(entity.getTimeSaved())
                .costSaved(entity.getCostSaved())
                .deliveriesCount(entity.getDeliveriesCount())
                .activeVehiclesCount(entity.getActiveVehiclesCount())
                .build();
    }
}
