package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.AnalyticsRouteSummary;
import com.rotacerta.domain.repository.AnalyticsRouteSummaryRepository;
import com.rotacerta.infrastructure.persistence.entity.AnalyticsRouteSummaryEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaAnalyticsRouteSummaryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AnalyticsRouteSummaryRepositoryImpl implements AnalyticsRouteSummaryRepository {

    private final JpaAnalyticsRouteSummaryRepository jpaRepository;

    public AnalyticsRouteSummaryRepositoryImpl(JpaAnalyticsRouteSummaryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AnalyticsRouteSummary save(AnalyticsRouteSummary summary) {
        AnalyticsRouteSummaryEntity entity = AnalyticsRouteSummaryEntity.builder()
                .id(summary.getId())
                .tenantId(summary.getTenantId())
                .routeId(summary.getRouteId())
                .vehicleId(summary.getVehicleId())
                .distanceOptimized(summary.getDistanceOptimized())
                .distanceOriginal(summary.getDistanceOriginal())
                .timeOptimized(summary.getTimeOptimized())
                .timeOriginal(summary.getTimeOriginal())
                .fuelConsumptionOptimized(summary.getFuelConsumptionOptimized())
                .fuelCostOptimized(summary.getFuelCostOptimized())
                .pointsCount(summary.getPointsCount())
                .capacityUtilization(summary.getCapacityUtilization())
                .status(summary.getStatus())
                .build();
        entity = jpaRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public List<AnalyticsRouteSummary> findByTenantIdAndDateRange(UUID tenantId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByTenantIdAndCreatedAtBetween(tenantId, start, end).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private AnalyticsRouteSummary mapToDomain(AnalyticsRouteSummaryEntity entity) {
        return AnalyticsRouteSummary.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .routeId(entity.getRouteId())
                .vehicleId(entity.getVehicleId())
                .distanceOptimized(entity.getDistanceOptimized())
                .distanceOriginal(entity.getDistanceOriginal())
                .timeOptimized(entity.getTimeOptimized())
                .timeOriginal(entity.getTimeOriginal())
                .fuelConsumptionOptimized(entity.getFuelConsumptionOptimized())
                .fuelCostOptimized(entity.getFuelCostOptimized())
                .pointsCount(entity.getPointsCount())
                .capacityUtilization(entity.getCapacityUtilization())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
