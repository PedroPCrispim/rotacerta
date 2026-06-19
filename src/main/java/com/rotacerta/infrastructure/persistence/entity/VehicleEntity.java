package com.rotacerta.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Double capacity;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Column(name = "avg_consumption", nullable = false)
    private Double avgConsumption;

    @Column(name = "cost_per_km")
    private BigDecimal costPerKm;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public VehicleEntity() {}

    public VehicleEntity(UUID id, UUID companyId, String plate, String model, Double capacity, String fuelType, Double avgConsumption, BigDecimal costPerKm, LocalDateTime createdAt) {
        this.id = id;
        this.companyId = companyId;
        this.plate = plate;
        this.model = model;
        this.capacity = capacity;
        this.fuelType = fuelType;
        this.avgConsumption = avgConsumption;
        this.costPerKm = costPerKm;
        this.createdAt = createdAt;
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
        private LocalDateTime createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }
        public Builder plate(String plate) { this.plate = plate; return this; }
        public Builder model(String model) { this.model = model; return this; }
        public Builder capacity(Double capacity) { this.capacity = capacity; return this; }
        public Builder fuelType(String fuelType) { this.fuelType = fuelType; return this; }
        public Builder avgConsumption(Double avgConsumption) { this.avgConsumption = avgConsumption; return this; }
        public Builder costPerKm(BigDecimal costPerKm) { this.costPerKm = costPerKm; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public VehicleEntity build() {
            return new VehicleEntity(id, companyId, plate, model, capacity, fuelType, avgConsumption, costPerKm, createdAt);
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
