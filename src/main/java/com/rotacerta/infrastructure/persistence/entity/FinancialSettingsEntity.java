package com.rotacerta.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_settings")
public class FinancialSettingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false, unique = true)
    private UUID companyId;

    @Column(nullable = false)
    private String region;

    @Column(name = "gasoline_price", nullable = false)
    private BigDecimal gasolinePrice;

    @Column(name = "ethanol_price", nullable = false)
    private BigDecimal ethanolPrice;

    @Column(name = "diesel_price", nullable = false)
    private BigDecimal dieselPrice;

    @Column(name = "fixed_cost_per_vehicle", nullable = false)
    private BigDecimal fixedCostPerVehicle;

    @Column(name = "maintenance_reserve", nullable = false)
    private BigDecimal maintenanceReserve;

    @Column(name = "toll_cost", nullable = false)
    private BigDecimal tollCost;

    @Column(name = "driver_daily_cost", nullable = false)
    private BigDecimal driverDailyCost;

    @Column(name = "target_savings", nullable = false)
    private BigDecimal targetSavings;

    @Column(name = "max_cost_per_delivery", nullable = false)
    private BigDecimal maxCostPerDelivery;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public FinancialSettingsEntity() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public BigDecimal getGasolinePrice() { return gasolinePrice; }
    public void setGasolinePrice(BigDecimal gasolinePrice) { this.gasolinePrice = gasolinePrice; }
    public BigDecimal getEthanolPrice() { return ethanolPrice; }
    public void setEthanolPrice(BigDecimal ethanolPrice) { this.ethanolPrice = ethanolPrice; }
    public BigDecimal getDieselPrice() { return dieselPrice; }
    public void setDieselPrice(BigDecimal dieselPrice) { this.dieselPrice = dieselPrice; }
    public BigDecimal getFixedCostPerVehicle() { return fixedCostPerVehicle; }
    public void setFixedCostPerVehicle(BigDecimal fixedCostPerVehicle) { this.fixedCostPerVehicle = fixedCostPerVehicle; }
    public BigDecimal getMaintenanceReserve() { return maintenanceReserve; }
    public void setMaintenanceReserve(BigDecimal maintenanceReserve) { this.maintenanceReserve = maintenanceReserve; }
    public BigDecimal getTollCost() { return tollCost; }
    public void setTollCost(BigDecimal tollCost) { this.tollCost = tollCost; }
    public BigDecimal getDriverDailyCost() { return driverDailyCost; }
    public void setDriverDailyCost(BigDecimal driverDailyCost) { this.driverDailyCost = driverDailyCost; }
    public BigDecimal getTargetSavings() { return targetSavings; }
    public void setTargetSavings(BigDecimal targetSavings) { this.targetSavings = targetSavings; }
    public BigDecimal getMaxCostPerDelivery() { return maxCostPerDelivery; }
    public void setMaxCostPerDelivery(BigDecimal maxCostPerDelivery) { this.maxCostPerDelivery = maxCostPerDelivery; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
