package com.rotacerta.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class FinancialSettings {
    private UUID id;
    private UUID companyId;
    private String region;
    private BigDecimal gasolinePrice;
    private BigDecimal ethanolPrice;
    private BigDecimal dieselPrice;
    private BigDecimal fixedCostPerVehicle;
    private BigDecimal maintenanceReserve;
    private BigDecimal tollCost;
    private BigDecimal driverDailyCost;
    private BigDecimal targetSavings;
    private BigDecimal maxCostPerDelivery;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FinancialSettings() {
    }

    public FinancialSettings(UUID id, UUID companyId, String region, BigDecimal gasolinePrice, BigDecimal ethanolPrice,
                             BigDecimal dieselPrice, BigDecimal fixedCostPerVehicle, BigDecimal maintenanceReserve,
                             BigDecimal tollCost, BigDecimal driverDailyCost, BigDecimal targetSavings,
                             BigDecimal maxCostPerDelivery, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyId = companyId;
        this.region = region;
        this.gasolinePrice = gasolinePrice;
        this.ethanolPrice = ethanolPrice;
        this.dieselPrice = dieselPrice;
        this.fixedCostPerVehicle = fixedCostPerVehicle;
        this.maintenanceReserve = maintenanceReserve;
        this.tollCost = tollCost;
        this.driverDailyCost = driverDailyCost;
        this.targetSavings = targetSavings;
        this.maxCostPerDelivery = maxCostPerDelivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID companyId;
        private String region;
        private BigDecimal gasolinePrice;
        private BigDecimal ethanolPrice;
        private BigDecimal dieselPrice;
        private BigDecimal fixedCostPerVehicle;
        private BigDecimal maintenanceReserve;
        private BigDecimal tollCost;
        private BigDecimal driverDailyCost;
        private BigDecimal targetSavings;
        private BigDecimal maxCostPerDelivery;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }
        public Builder region(String region) { this.region = region; return this; }
        public Builder gasolinePrice(BigDecimal gasolinePrice) { this.gasolinePrice = gasolinePrice; return this; }
        public Builder ethanolPrice(BigDecimal ethanolPrice) { this.ethanolPrice = ethanolPrice; return this; }
        public Builder dieselPrice(BigDecimal dieselPrice) { this.dieselPrice = dieselPrice; return this; }
        public Builder fixedCostPerVehicle(BigDecimal fixedCostPerVehicle) { this.fixedCostPerVehicle = fixedCostPerVehicle; return this; }
        public Builder maintenanceReserve(BigDecimal maintenanceReserve) { this.maintenanceReserve = maintenanceReserve; return this; }
        public Builder tollCost(BigDecimal tollCost) { this.tollCost = tollCost; return this; }
        public Builder driverDailyCost(BigDecimal driverDailyCost) { this.driverDailyCost = driverDailyCost; return this; }
        public Builder targetSavings(BigDecimal targetSavings) { this.targetSavings = targetSavings; return this; }
        public Builder maxCostPerDelivery(BigDecimal maxCostPerDelivery) { this.maxCostPerDelivery = maxCostPerDelivery; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public FinancialSettings build() {
            return new FinancialSettings(id, companyId, region, gasolinePrice, ethanolPrice, dieselPrice,
                    fixedCostPerVehicle, maintenanceReserve, tollCost, driverDailyCost,
                    targetSavings, maxCostPerDelivery, createdAt, updatedAt);
        }
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
