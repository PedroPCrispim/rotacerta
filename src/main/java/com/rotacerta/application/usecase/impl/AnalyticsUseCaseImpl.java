package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.*;
import com.rotacerta.application.usecase.AnalyticsUseCase;
import com.rotacerta.domain.model.AnalyticsDailyAggregator;
import com.rotacerta.domain.model.AnalyticsRouteSummary;
import com.rotacerta.domain.model.FinancialSettings;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.AnalyticsDailyAggregatorRepository;
import com.rotacerta.domain.repository.AnalyticsRouteSummaryRepository;
import com.rotacerta.domain.repository.FinancialSettingsRepository;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
    private final FinancialSettingsRepository financialSettingsRepository;

    public AnalyticsUseCaseImpl(AnalyticsRouteSummaryRepository routeSummaryRepository,
                                AnalyticsDailyAggregatorRepository dailyAggregatorRepository,
                                VehicleRepository vehicleRepository,
                                FinancialSettingsRepository financialSettingsRepository) {
        this.routeSummaryRepository = routeSummaryRepository;
        this.dailyAggregatorRepository = dailyAggregatorRepository;
        this.vehicleRepository = vehicleRepository;
        this.financialSettingsRepository = financialSettingsRepository;
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
    public FinancialSettingsDTO getFinancialSettings() {
        UUID tenantId = TenantContext.getTenantId();
        return financialSettingsRepository.findByCompanyId(tenantId)
                .map(this::mapToFinancialSettingsDTO)
                .orElseGet(() -> mapToFinancialSettingsDTO(buildDefaultFinancialSettings(tenantId)));
    }

    @Override
    public FinancialSettingsDTO saveFinancialSettings(FinancialSettingsDTO dto) {
        UUID tenantId = TenantContext.getTenantId();
        FinancialSettings existing = financialSettingsRepository.findByCompanyId(tenantId)
                .orElseGet(() -> buildDefaultFinancialSettings(tenantId));

        existing.setCompanyId(tenantId);
        existing.setRegion(dto.getRegion());
        existing.setGasolinePrice(dto.getGasolinePrice());
        existing.setEthanolPrice(dto.getEthanolPrice());
        existing.setDieselPrice(dto.getDieselPrice());
        existing.setFixedCostPerVehicle(dto.getFixedCostPerVehicle());
        existing.setMaintenanceReserve(dto.getMaintenanceReserve());
        existing.setTollCost(dto.getTollCost());
        existing.setDriverDailyCost(dto.getDriverDailyCost());
        existing.setTargetSavings(dto.getTargetSavings());
        existing.setMaxCostPerDelivery(dto.getMaxCostPerDelivery());

        return mapToFinancialSettingsDTO(financialSettingsRepository.save(existing));
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

    @Override
    public byte[] exportReport(LocalDate start, LocalDate end, String granularity, String view, UUID vehicleId) {
        String normalizedGranularity = normalizeGranularity(granularity);
        String normalizedView = normalizeView(view);

        StringBuilder csv = new StringBuilder("\uFEFF");
        csv.append("secao;chave;valor\n");
        csv.append("metadados;visao;").append(escapeCsv(normalizedView)).append("\n");
        csv.append("metadados;periodo_inicial;").append(start).append("\n");
        csv.append("metadados;periodo_final;").append(end).append("\n");
        csv.append("metadados;granularidade;").append(normalizedGranularity).append("\n");
        csv.append("metadados;veiculo_id;").append(vehicleId == null ? "todos" : vehicleId).append("\n");

        switch (normalizedView) {
            case "financial":
                appendFinancialExport(csv, start, end, normalizedGranularity, vehicleId);
                break;
            case "fleet":
                appendFleetExport(csv, start, end);
                break;
            case "operational":
            default:
                appendOperationalExport(csv, start, end, normalizedGranularity, vehicleId);
                break;
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
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

    private String normalizeView(String view) {
        if (view == null) {
            return "operational";
        }

        switch (view.toLowerCase(Locale.ROOT)) {
            case "financial":
            case "fleet":
            case "operational":
                return view.toLowerCase(Locale.ROOT);
            default:
                return "operational";
        }
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

    private void appendOperationalExport(StringBuilder csv, LocalDate start, LocalDate end, String granularity, UUID vehicleId) {
        DashboardDTO dashboard = getDashboard(start, end);
        OperationsAnalyticsDTO operations = getOperations(start, end);

        csv.append("resumo_operacional;total_rotas;").append(operations.getTotalRoutes()).append("\n");
        csv.append("resumo_operacional;total_entregas;").append(operations.getTotalDeliveries()).append("\n");
        csv.append("resumo_operacional;km_economizados;").append(formatDouble(dashboard.getTotalKmSaved())).append("\n");
        csv.append("resumo_operacional;tempo_economizado_segundos;").append(dashboard.getTotalTimeSaved()).append("\n");
        csv.append("resumo_operacional;utilizacao_media_percentual;").append(formatDouble(dashboard.getAvgCapacityUtilization())).append("\n");

        appendTimelineSection(csv, "timeline_operacional_distancia", getTimeline(start, end, granularity, "distanceSaved", vehicleId));
        appendTimelineSection(csv, "timeline_operacional_tempo", getTimeline(start, end, granularity, "timeSaved", vehicleId));
        appendTimelineSection(csv, "timeline_operacional_entregas", getTimeline(start, end, granularity, "deliveries", vehicleId));
    }

    private void appendFinancialExport(StringBuilder csv, LocalDate start, LocalDate end, String granularity, UUID vehicleId) {
        DashboardDTO dashboard = getDashboard(start, end);
        FinancialAnalyticsDTO financial = getFinancial(start, end);
        FinancialSettingsDTO settings = getFinancialSettings();

        csv.append("resumo_financeiro;economia_total;").append(formatBigDecimal(financial.getTotalCostSaved())).append("\n");
        csv.append("resumo_financeiro;economia_dashboard;").append(formatBigDecimal(dashboard.getTotalCostSaved())).append("\n");
        csv.append("resumo_financeiro;regiao;").append(escapeCsv(settings.getRegion())).append("\n");
        csv.append("resumo_financeiro;gasolina;").append(formatBigDecimal(settings.getGasolinePrice())).append("\n");
        csv.append("resumo_financeiro;etanol;").append(formatBigDecimal(settings.getEthanolPrice())).append("\n");
        csv.append("resumo_financeiro;diesel;").append(formatBigDecimal(settings.getDieselPrice())).append("\n");
        csv.append("resumo_financeiro;custo_fixo_por_veiculo;").append(formatBigDecimal(settings.getFixedCostPerVehicle())).append("\n");
        csv.append("resumo_financeiro;reserva_manutencao;").append(formatBigDecimal(settings.getMaintenanceReserve())).append("\n");
        csv.append("resumo_financeiro;pedagio_e_extras;").append(formatBigDecimal(settings.getTollCost())).append("\n");
        csv.append("resumo_financeiro;motorista_dia;").append(formatBigDecimal(settings.getDriverDailyCost())).append("\n");
        csv.append("resumo_financeiro;meta_economia;").append(formatBigDecimal(settings.getTargetSavings())).append("\n");
        csv.append("resumo_financeiro;teto_por_entrega;").append(formatBigDecimal(settings.getMaxCostPerDelivery())).append("\n");

        appendTimelineSection(csv, "timeline_financeiro_custo", getTimeline(start, end, granularity, "costSaved", vehicleId));
        appendTimelineSection(csv, "timeline_financeiro_distancia", getTimeline(start, end, granularity, "distanceSaved", vehicleId));
        appendTimelineSection(csv, "timeline_financeiro_tempo", getTimeline(start, end, granularity, "timeSaved", vehicleId));
        appendTimelineSection(csv, "timeline_financeiro_entregas", getTimeline(start, end, granularity, "deliveries", vehicleId));
    }

    private void appendFleetExport(StringBuilder csv, LocalDate start, LocalDate end) {
        FleetAnalyticsDTO fleet = getFleet(start, end);

        csv.append("resumo_frota;total_veiculos;").append(fleet.getTotalVehicles()).append("\n");
        csv.append("resumo_frota;utilizacao_media_percentual;").append(formatDouble(fleet.getAvgUtilization())).append("\n");
        csv.append("frota;veiculo_id;distancia_total_metros;utilizacao_media_percentual\n");

        for (VehiclePerformanceDTO performance : fleet.getVehiclePerformances()) {
            csv.append("frota;")
                    .append(performance.getVehicleId())
                    .append(";")
                    .append(performance.getTotalDistance() == null ? 0 : performance.getTotalDistance())
                    .append(";")
                    .append(formatDouble(performance.getAvgCapacityUtilization()))
                    .append("\n");
        }
    }

    private void appendTimelineSection(StringBuilder csv, String sectionName, AnalyticsTimelineDTO timeline) {
        csv.append(sectionName)
                .append(";total_atual;")
                .append(formatDouble(timeline.getCurrentTotal()))
                .append("\n");
        csv.append(sectionName)
                .append(";total_anterior;")
                .append(formatDouble(timeline.getPreviousTotal()))
                .append("\n");
        csv.append(sectionName)
                .append(";variacao_percentual;")
                .append(formatDouble(timeline.getVariationPercentage()))
                .append("\n");
        csv.append(sectionName).append(";bucket_label;bucket_comparacao;valor_atual;valor_anterior\n");

        for (AnalyticsTimelinePointDTO point : timeline.getPoints()) {
            csv.append(sectionName)
                    .append(";")
                    .append(escapeCsv(point.getLabel()))
                    .append(";")
                    .append(escapeCsv(point.getComparisonLabel()))
                    .append(";")
                    .append(formatDouble(point.getCurrentValue()))
                    .append(";")
                    .append(formatDouble(point.getPreviousValue()))
                    .append("\n");
        }
    }

    private String formatDouble(Double value) {
        return value == null ? "0" : BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }

    private String formatBigDecimal(BigDecimal value) {
        return value == null ? "0" : value.stripTrailingZeros().toPlainString();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private FinancialSettings buildDefaultFinancialSettings(UUID companyId) {
        return FinancialSettings.builder()
                .companyId(companyId)
                .region("Sao Paulo")
                .gasolinePrice(new BigDecimal("6.19"))
                .ethanolPrice(new BigDecimal("4.09"))
                .dieselPrice(new BigDecimal("6.02"))
                .fixedCostPerVehicle(new BigDecimal("1800.00"))
                .maintenanceReserve(new BigDecimal("650.00"))
                .tollCost(new BigDecimal("280.00"))
                .driverDailyCost(new BigDecimal("180.00"))
                .targetSavings(new BigDecimal("4500.00"))
                .maxCostPerDelivery(new BigDecimal("22.00"))
                .build();
    }

    private FinancialSettingsDTO mapToFinancialSettingsDTO(FinancialSettings settings) {
        FinancialSettingsDTO dto = new FinancialSettingsDTO();
        dto.setId(settings.getId());
        dto.setRegion(settings.getRegion());
        dto.setGasolinePrice(settings.getGasolinePrice());
        dto.setEthanolPrice(settings.getEthanolPrice());
        dto.setDieselPrice(settings.getDieselPrice());
        dto.setFixedCostPerVehicle(settings.getFixedCostPerVehicle());
        dto.setMaintenanceReserve(settings.getMaintenanceReserve());
        dto.setTollCost(settings.getTollCost());
        dto.setDriverDailyCost(settings.getDriverDailyCost());
        dto.setTargetSavings(settings.getTargetSavings());
        dto.setMaxCostPerDelivery(settings.getMaxCostPerDelivery());
        return dto;
    }
}
