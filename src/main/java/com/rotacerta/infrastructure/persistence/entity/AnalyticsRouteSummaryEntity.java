package com.rotacerta.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_route_summaries")
public class AnalyticsRouteSummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "route_id", nullable = false)
    private UUID routeId;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Column(name = "distance_optimized")
    private Long distanceOptimized;

    @Column(name = "distance_original")
    private Long distanceOriginal;

    @Column(name = "time_optimized")
    private Long timeOptimized;

    @Column(name = "time_original")
    private Long timeOriginal;

    @Column(name = "fuel_consumption_optimized")
    private Double fuelConsumptionOptimized;

    @Column(name = "fuel_cost_optimized", precision = 19, scale = 2)
    private BigDecimal fuelCostOptimized;

    @Column(name = "points_count")
    private Integer pointsCount;

    @Column(name = "capacity_utilization")
    private Double capacityUtilization;

    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AnalyticsRouteSummaryEntity() {}

    public AnalyticsRouteSummaryEntity(UUID id, UUID tenantId, UUID routeId, UUID vehicleId, Long distanceOptimized, Long distanceOriginal, Long timeOptimized, Long timeOriginal, Double fuelConsumptionOptimized, BigDecimal fuelCostOptimized, Integer pointsCount, Double capacityUtilization, String status, LocalDateTime createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.routeId = routeId;
        this.vehicleId = vehicleId;
        this.distanceOptimized = distanceOptimized;
        this.distanceOriginal = distanceOriginal;
        this.timeOptimized = timeOptimized;
        this.timeOriginal = timeOriginal;
        this.fuelConsumptionOptimized = fuelConsumptionOptimized;
        this.fuelCostOptimized = fuelCostOptimized;
        this.pointsCount = pointsCount;
        this.capacityUtilization = capacityUtilization;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID tenantId;
        private UUID routeId;
        private UUID vehicleId;
        private Long distanceOptimized;
        private Long distanceOriginal;
        private Long timeOptimized;
        private Long timeOriginal;
        private Double fuelConsumptionOptimized;
        private BigDecimal fuelCostOptimized;
        private Integer pointsCount;
        private Double capacityUtilization;
        private String status;
        private LocalDateTime createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder tenantId(UUID tenantId) { this.tenantId = tenantId; return this; }
        public Builder routeId(UUID routeId) { this.routeId = routeId; return this; }
        public Builder vehicleId(UUID vehicleId) { this.vehicleId = vehicleId; return this; }
        public Builder distanceOptimized(Long distanceOptimized) { this.distanceOptimized = distanceOptimized; return this; }
        public Builder distanceOriginal(Long distanceOriginal) { this.distanceOriginal = distanceOriginal; return this; }
        public Builder timeOptimized(Long timeOptimized) { this.timeOptimized = timeOptimized; return this; }
        public Builder timeOriginal(Long timeOriginal) { this.timeOriginal = timeOriginal; return this; }
        public Builder fuelConsumptionOptimized(Double fuelConsumptionOptimized) { this.fuelConsumptionOptimized = fuelConsumptionOptimized; return this; }
        public Builder fuelCostOptimized(BigDecimal fuelCostOptimized) { this.fuelCostOptimized = fuelCostOptimized; return this; }
        public Builder pointsCount(Integer pointsCount) { this.pointsCount = pointsCount; return this; }
        public Builder capacityUtilization(Double capacityUtilization) { this.capacityUtilization = capacityUtilization; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AnalyticsRouteSummaryEntity build() {
            return new AnalyticsRouteSummaryEntity(id, tenantId, routeId, vehicleId, distanceOptimized, distanceOriginal, timeOptimized, timeOriginal, fuelConsumptionOptimized, fuelCostOptimized, pointsCount, capacityUtilization, status, createdAt);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public UUID getRouteId() { return routeId; }
    public void setRouteId(UUID routeId) { this.routeId = routeId; }
    public UUID getVehicleId() { return vehicleId; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }
    public Long getDistanceOptimized() { return distanceOptimized; }
    public void setDistanceOptimized(Long distanceOptimized) { this.distanceOptimized = distanceOptimized; }
    public Long getDistanceOriginal() { return distanceOriginal; }
    public void setDistanceOriginal(Long distanceOriginal) { this.distanceOriginal = distanceOriginal; }
    public Long getTimeOptimized() { return timeOptimized; }
    public void setTimeOptimized(Long timeOptimized) { this.timeOptimized = timeOptimized; }
    public Long getTimeOriginal() { return timeOriginal; }
    public void setTimeOriginal(Long timeOriginal) { this.timeOriginal = timeOriginal; }
    public Double getFuelConsumptionOptimized() { return fuelConsumptionOptimized; }
    public void setFuelConsumptionOptimized(Double fuelConsumptionOptimized) { this.fuelConsumptionOptimized = fuelConsumptionOptimized; }
    public BigDecimal getFuelCostOptimized() { return fuelCostOptimized; }
    public void setFuelCostOptimized(BigDecimal fuelCostOptimized) { this.fuelCostOptimized = fuelCostOptimized; }
    public Integer getPointsCount() { return pointsCount; }
    public void setPointsCount(Integer pointsCount) { this.pointsCount = pointsCount; }
    public Double getCapacityUtilization() { return capacityUtilization; }
    public void setCapacityUtilization(Double capacityUtilization) { this.capacityUtilization = capacityUtilization; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
