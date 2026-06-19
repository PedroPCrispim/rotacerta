package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.*;
import com.rotacerta.application.usecase.AnalyticsUseCase;
import com.rotacerta.domain.model.AnalyticsDailyAggregator;
import com.rotacerta.domain.model.AnalyticsRouteSummary;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.AnalyticsDailyAggregatorRepository;
import com.rotacerta.domain.repository.AnalyticsRouteSummaryRepository;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsUseCaseImpl implements AnalyticsUseCase {

    private final AnalyticsRouteSummaryRepository routeSummaryRepository;
    private final AnalyticsDailyAggregatorRepository dailyAggregatorRepository;
    private final VehicleRepository vehicleRepository;

    public AnalyticsUseCaseImpl(AnalyticsRouteSummaryRepository routeSummaryRepository,
                                AnalyticsDailyAggregatorRepository dailyAggregatorRepository,
                                VehicleRepository vehicleRepository) {
        this.routeSummaryRepository = routeSummaryRepository;
        this.dailyAggregatorRepository = dailyAggregatorRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public DashboardDTO getDashboard(LocalDate start, LocalDate end) {
        UUID tenantId = TenantContext.getTenantId();
        List<AnalyticsDailyAggregator> dailyData = dailyAggregatorRepository.findByTenantIdAndDateRange(tenantId, start, end);

        double totalKmSaved = dailyData.stream().mapToDouble(d -> d.getKmSaved()).sum();
        long totalTimeSaved = dailyData.stream().mapToLong(d -> d.getTimeSaved()).sum();
        BigDecimal totalCostSaved = dailyData.stream()
                .map(d -> d.getCostSaved())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalDeliveries = dailyData.stream().mapToInt(d -> d.getDeliveriesCount()).sum();

        List<AnalyticsRouteSummary> routes = routeSummaryRepository.findByTenantIdAndDateRange(tenantId, start.atStartOfDay(), end.atTime(23, 59, 59));
        double avgUtilization = routes.stream()
                .mapToDouble(r -> r.getCapacityUtilization())
                .average()
                .orElse(0.0);

        return DashboardDTO.builder()
                .totalKmSaved(totalKmSaved)
                .totalTimeSaved(totalTimeSaved)
                .totalCostSaved(totalCostSaved)
                .totalDeliveries(totalDeliveries)
                .avgCapacityUtilization(avgUtilization)
                .build();
    }

    @Override
    public FinancialAnalyticsDTO getFinancial(LocalDate start, LocalDate end) {
        UUID tenantId = TenantContext.getTenantId();
        List<AnalyticsDailyAggregator> dailyData = dailyAggregatorRepository.findByTenantIdAndDateRange(tenantId, start, end);

        BigDecimal totalCostSaved = dailyData.stream()
                .map(d -> d.getCostSaved())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> savingsByMonth = dailyData.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getReferenceDate().getMonth().name(),
                        Collectors.reducing(BigDecimal.ZERO, d -> d.getCostSaved(), BigDecimal::add)
                ));

        return FinancialAnalyticsDTO.builder()
                .totalCostSaved(totalCostSaved)
                .savingsByMonth(savingsByMonth)
                .build();
    }

    @Override
    public FleetAnalyticsDTO getFleet(LocalDate start, LocalDate end) {
        UUID tenantId = TenantContext.getTenantId();
        List<AnalyticsRouteSummary> routes = routeSummaryRepository.findByTenantIdAndDateRange(tenantId, start.atStartOfDay(), end.atTime(23, 59, 59));

        Map<UUID, List<AnalyticsRouteSummary>> byVehicle = routes.stream()
                .collect(Collectors.groupingBy(r -> r.getVehicleId()));

        List<VehiclePerformanceDTO> performances = byVehicle.entrySet().stream()
                .map(entry -> VehiclePerformanceDTO.builder()
                        .vehicleId(entry.getKey())
                        .totalDistance(entry.getValue().stream().mapToLong(r -> r.getDistanceOptimized()).sum())
                        .avgCapacityUtilization(entry.getValue().stream().mapToDouble(r -> r.getCapacityUtilization()).average().orElse(0.0))
                        .build())
                .collect(Collectors.toList());

        return FleetAnalyticsDTO.builder()
                .totalVehicles(byVehicle.size())
                .avgUtilization(routes.stream().mapToDouble(r -> r.getCapacityUtilization()).average().orElse(0.0))
                .vehiclePerformances(performances)
                .build();
    }

    @Override
    public OperationsAnalyticsDTO getOperations(LocalDate start, LocalDate end) {
        UUID tenantId = TenantContext.getTenantId();
        List<AnalyticsDailyAggregator> dailyData = dailyAggregatorRepository.findByTenantIdAndDateRange(tenantId, start, end);

        int totalDeliveries = dailyData.stream().mapToInt(d -> d.getDeliveriesCount()).sum();
        Map<String, Integer> deliveriesByDay = dailyData.stream()
                .collect(Collectors.toMap(
                        d -> d.getReferenceDate().toString(),
                        d -> d.getDeliveriesCount()
                ));

        List<AnalyticsRouteSummary> routes = routeSummaryRepository.findByTenantIdAndDateRange(tenantId, start.atStartOfDay(), end.atTime(23, 59, 59));

        return OperationsAnalyticsDTO.builder()
                .totalRoutes(routes.size())
                .totalDeliveries(totalDeliveries)
                .deliveriesByDay(deliveriesByDay)
                .build();
    }

    @Override
    public AnalyticsTimelineDTO getTimeline(LocalDate start, LocalDate end, String granularity, String metric, UUID vehicleId) {
        UUID tenantId = TenantContext.getTenantId();
        String normalizedGranularity = normalizeGranularity(granularity);
        String normalizedMetric = normalizeMetric(metric);

        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        LocalDate previousStart = start.minusDays(totalDays);
        LocalDate previousEnd = start.minusDays(1);

        List<AnalyticsRouteSummary> currentRoutes = routeSummaryRepository
                .findByTenantIdAndDateRange(tenantId, start.atStartOfDay(), end.atTime(23, 59, 59));
        List<AnalyticsRouteSummary> previousRoutes = routeSummaryRepository
                .findByTenantIdAndDateRange(tenantId, previousStart.atStartOfDay(), previousEnd.atTime(23, 59, 59));

        if (vehicleId != null) {
            currentRoutes = currentRoutes.stream()
                    .filter(route -> vehicleId.equals(route.getVehicleId()))
                    .collect(Collectors.toList());
            previousRoutes = previousRoutes.stream()
                    .filter(route -> vehicleId.equals(route.getVehicleId()))
                    .collect(Collectors.toList());
        }

        Map<UUID, Vehicle> vehiclesById = vehicleRepository.findAllByCompanyId(tenantId).stream()
                .collect(Collectors.toMap(Vehicle::getId, vehicle -> vehicle));

        List<String> currentBucketKeys = buildBucketKeys(start, end, normalizedGranularity);
        List<String> previousBucketKeys = buildBucketKeys(previousStart, previousEnd, normalizedGranularity);

        Map<String, Double> currentValues = aggregateRoutesByBucket(currentRoutes, normalizedGranularity, normalizedMetric, vehiclesById);
        Map<String, Double> previousValues = aggregateRoutesByBucket(previousRoutes, normalizedGranularity, normalizedMetric, vehiclesById);

        List<AnalyticsTimelinePointDTO> points = new ArrayList<>();
        for (int i = 0; i < currentBucketKeys.size(); i++) {
            String currentBucketKey = currentBucketKeys.get(i);
            String previousBucketKey = i < previousBucketKeys.size() ? previousBucketKeys.get(i) : null;
            points.add(AnalyticsTimelinePointDTO.builder()
                    .label(formatBucketLabel(currentBucketKey, normalizedGranularity))
                    .comparisonLabel(previousBucketKey == null ? null : formatBucketLabel(previousBucketKey, normalizedGranularity))
                    .currentValue(currentValues.getOrDefault(currentBucketKey, 0.0))
                    .previousValue(previousBucketKey == null ? 0.0 : previousValues.getOrDefault(previousBucketKey, 0.0))
                    .build());
        }

        double currentTotal = points.stream().mapToDouble(point -> point.getCurrentValue() == null ? 0.0 : point.getCurrentValue()).sum();
        double previousTotal = points.stream().mapToDouble(point -> point.getPreviousValue() == null ? 0.0 : point.getPreviousValue()).sum();
        double variationPercentage = previousTotal == 0.0
                ? (currentTotal > 0.0 ? 100.0 : 0.0)
                : ((currentTotal - previousTotal) / previousTotal) * 100.0;

        return AnalyticsTimelineDTO.builder()
                .metric(normalizedMetric)
                .granularity(normalizedGranularity)
                .currentTotal(currentTotal)
                .previousTotal(previousTotal)
                .variationPercentage(variationPercentage)
                .points(points)
                .build();
    }

    private Map<String, Double> aggregateRoutesByBucket(List<AnalyticsRouteSummary> routes,
                                                        String granularity,
                                                        String metric,
                                                        Map<UUID, Vehicle> vehiclesById) {
        return routes.stream()
                .sorted(Comparator.comparing(AnalyticsRouteSummary::getCreatedAt))
                .collect(Collectors.toMap(
                        route -> buildBucketKey(route.getCreatedAt(), granularity),
                        route -> extractMetricValue(route, metric, vehiclesById),
                        Double::sum,
                        LinkedHashMap::new
                ));
    }

    private double extractMetricValue(AnalyticsRouteSummary route, String metric, Map<UUID, Vehicle> vehiclesById) {
        long distanceSavedMeters = Math.max(0L, defaultLong(route.getDistanceOriginal()) - defaultLong(route.getDistanceOptimized()));
        long timeSavedSeconds = Math.max(0L, defaultLong(route.getTimeOriginal()) - defaultLong(route.getTimeOptimized()));

        switch (metric) {
            case "distanceSaved":
                return distanceSavedMeters / 1000.0;
            case "timeSaved":
                return timeSavedSeconds / 3600.0;
            case "deliveries":
                return route.getPointsCount() == null ? 0.0 : route.getPointsCount().doubleValue();
            case "costSaved":
            default:
                Vehicle vehicle = vehiclesById.get(route.getVehicleId());
                BigDecimal costPerKm = vehicle != null && vehicle.getCostPerKm() != null
                        ? vehicle.getCostPerKm()
                        : BigDecimal.ZERO;
                return BigDecimal.valueOf(distanceSavedMeters / 1000.0)
                        .multiply(costPerKm)
                        .doubleValue();
        }
    }

    private List<String> buildBucketKeys(LocalDate start, LocalDate end, String granularity) {
        List<String> keys = new ArrayList<>();

        switch (granularity) {
            case "year":
                for (int year = start.getYear(); year <= end.getYear(); year++) {
                    keys.add(String.valueOf(year));
                }
                break;
            case "month":
                YearMonth currentMonth = YearMonth.from(start);
                YearMonth endMonth = YearMonth.from(end);
                while (!currentMonth.isAfter(endMonth)) {
                    keys.add(currentMonth.toString());
                    currentMonth = currentMonth.plusMonths(1);
                }
                break;
            case "day":
            default:
                LocalDate currentDate = start;
                while (!currentDate.isAfter(end)) {
                    keys.add(currentDate.toString());
                    currentDate = currentDate.plusDays(1);
                }
                break;
        }

        return keys;
    }

    private String buildBucketKey(LocalDateTime dateTime, String granularity) {
        switch (granularity) {
            case "year":
                return String.valueOf(dateTime.getYear());
            case "month":
                return YearMonth.from(dateTime).toString();
            case "day":
            default:
                return dateTime.toLocalDate().toString();
        }
    }

    private String formatBucketLabel(String bucketKey, String granularity) {
        Locale locale = Locale.forLanguageTag("pt-BR");
        switch (granularity) {
            case "year":
                return bucketKey;
            case "month":
                return YearMonth.parse(bucketKey).format(DateTimeFormatter.ofPattern("MMM/yy", locale));
            case "day":
            default:
                return LocalDate.parse(bucketKey).format(DateTimeFormatter.ofPattern("dd/MM", locale));
        }
    }

    private String normalizeGranularity(String granularity) {
        if (granularity == null) {
            return "month";
        }

        switch (granularity.toLowerCase(Locale.ROOT)) {
            case "day":
            case "month":
            case "year":
                return granularity.toLowerCase(Locale.ROOT);
            default:
                return "month";
        }
    }

    private String normalizeMetric(String metric) {
        if (metric == null) {
            return "costSaved";
        }

        switch (metric) {
            case "distanceSaved":
            case "timeSaved":
            case "deliveries":
            case "costSaved":
                return metric;
            default:
                return "costSaved";
        }
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}
