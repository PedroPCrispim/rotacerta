package com.rotacerta.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "analytics_daily_aggregators")
public class AnalyticsDailyAggregatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    @Column(name = "km_saved")
    private Double kmSaved;

    @Column(name = "time_saved")
    private Long timeSaved;

    @Column(name = "cost_saved", precision = 19, scale = 2)
    private BigDecimal costSaved;

    @Column(name = "deliveries_count")
    private Integer deliveriesCount;

    @Column(name = "active_vehicles_count")
    private Integer activeVehiclesCount;

    public AnalyticsDailyAggregatorEntity() {}

    public AnalyticsDailyAggregatorEntity(UUID id, UUID tenantId, LocalDate referenceDate, Double kmSaved, Long timeSaved, BigDecimal costSaved, Integer deliveriesCount, Integer activeVehiclesCount) {
        this.id = id;
        this.tenantId = tenantId;
        this.referenceDate = referenceDate;
        this.kmSaved = kmSaved;
        this.timeSaved = timeSaved;
        this.costSaved = costSaved;
        this.deliveriesCount = deliveriesCount;
        this.activeVehiclesCount = activeVehiclesCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID tenantId;
        private LocalDate referenceDate;
        private Double kmSaved;
        private Long timeSaved;
        private BigDecimal costSaved;
        private Integer deliveriesCount;
        private Integer activeVehiclesCount;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder tenantId(UUID tenantId) { this.tenantId = tenantId; return this; }
        public Builder referenceDate(LocalDate referenceDate) { this.referenceDate = referenceDate; return this; }
        public Builder kmSaved(Double kmSaved) { this.kmSaved = kmSaved; return this; }
        public Builder timeSaved(Long timeSaved) { this.timeSaved = timeSaved; return this; }
        public Builder costSaved(BigDecimal costSaved) { this.costSaved = costSaved; return this; }
        public Builder deliveriesCount(Integer deliveriesCount) { this.deliveriesCount = deliveriesCount; return this; }
        public Builder activeVehiclesCount(Integer activeVehiclesCount) { this.activeVehiclesCount = activeVehiclesCount; return this; }

        public AnalyticsDailyAggregatorEntity build() {
            return new AnalyticsDailyAggregatorEntity(id, tenantId, referenceDate, kmSaved, timeSaved, costSaved, deliveriesCount, activeVehiclesCount);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public LocalDate getReferenceDate() { return referenceDate; }
    public void setReferenceDate(LocalDate referenceDate) { this.referenceDate = referenceDate; }
    public Double getKmSaved() { return kmSaved; }
    public void setKmSaved(Double kmSaved) { this.kmSaved = kmSaved; }
    public Long getTimeSaved() { return timeSaved; }
    public void setTimeSaved(Long timeSaved) { this.timeSaved = timeSaved; }
    public BigDecimal getCostSaved() { return costSaved; }
    public void setCostSaved(BigDecimal costSaved) { this.costSaved = costSaved; }
    public Integer getDeliveriesCount() { return deliveriesCount; }
    public void setDeliveriesCount(Integer deliveriesCount) { this.deliveriesCount = deliveriesCount; }
    public Integer getActiveVehiclesCount() { return activeVehiclesCount; }
    public void setActiveVehiclesCount(Integer activeVehiclesCount) { this.activeVehiclesCount = activeVehiclesCount; }
}
