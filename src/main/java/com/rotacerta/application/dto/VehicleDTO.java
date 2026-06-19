package com.rotacerta.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class VehicleDTO {
    private UUID id;

    @NotBlank(message = "Placa é obrigatória")
    private String plate;

    @NotBlank(message = "Modelo é obrigatório")
    private String model;

    @NotNull(message = "Capacidade é obrigatória")
    @Positive(message = "Capacidade deve ser positiva")
    private Double capacity;

    @NotBlank(message = "Tipo de combustível é obrigatório")
    private String fuelType;

    @NotNull(message = "Consumo médio é obrigatório")
    @Positive(message = "Consumo médio deve ser positivo")
    private Double avgConsumption;

    private BigDecimal costPerKm;

    public VehicleDTO() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
