package com.rotacerta.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Vehicle {
    private UUID id;
    private UUID companyId;
    private String plate;
    private String model;
    private Double capacity;
    private String fuelType;
    private Double avgConsumption;
    private BigDecimal costPerKm;

    public Vehicle() {}

    public Vehicle(UUID id, UUID companyId, String plate, String model, Double capacity, String fuelType, Double avgConsumption, BigDecimal costPerKm) {
        this.id = id;
        this.companyId = companyId;
        this.plate = plate;
        this.model = model;
        this.capacity = capacity;
        this.fuelType = fuelType;
        this.avgConsumption = avgConsumption;
        this.costPerKm = costPerKm;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID companyId;
        private String plate;
        private String model;
        private Double capacity;
        private String fuelType;
        private Double avgConsumption;
        private BigDecimal costPerKm;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }
        public Builder plate(String plate) { this.plate = plate; return this; }
        public Builder model(String model) { this.model = model; return this; }
        public Builder capacity(Double capacity) { this.capacity = capacity; return this; }
        public Builder fuelType(String fuelType) { this.fuelType = fuelType; return this; }
        public Builder avgConsumption(Double avgConsumption) { this.avgConsumption = avgConsumption; return this; }
        public Builder costPerKm(BigDecimal costPerKm) { this.costPerKm = costPerKm; return this; }

        public Vehicle build() {
            return new Vehicle(id, companyId, plate, model, capacity, fuelType, avgConsumption, costPerKm);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public Double getAvgConsumption() { return avgConsumption; }
    public void setAvgConsumption(Double avgConsumption) { this.avgConsumption = avgConsumption; }
    public BigDecimal getCostPerKm() { return costPerKm; }
    public void setCostPerKm(BigDecimal costPerKm) { this.costPerKm = costPerKm; }
}
