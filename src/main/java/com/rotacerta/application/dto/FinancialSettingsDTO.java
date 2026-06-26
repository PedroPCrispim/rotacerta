package com.rotacerta.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class FinancialSettingsDTO {
    private UUID id;

    @NotBlank(message = "Região é obrigatória")
    private String region;

    @NotNull(message = "Preço da gasolina é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço da gasolina deve ser maior que zero")
    private BigDecimal gasolinePrice;

    @NotNull(message = "Preço do etanol é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço do etanol deve ser maior que zero")
    private BigDecimal ethanolPrice;

    @NotNull(message = "Preço do diesel é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço do diesel deve ser maior que zero")
    private BigDecimal dieselPrice;

    @NotNull(message = "Custo fixo por veículo é obrigatório")
    @DecimalMin(value = "0.0", message = "Custo fixo por veículo não pode ser negativo")
    private BigDecimal fixedCostPerVehicle;

    @NotNull(message = "Reserva de manutenção é obrigatória")
    @DecimalMin(value = "0.0", message = "Reserva de manutenção não pode ser negativa")
    private BigDecimal maintenanceReserve;

    @NotNull(message = "Pedágio e extras são obrigatórios")
    @DecimalMin(value = "0.0", message = "Pedágio e extras não podem ser negativos")
    private BigDecimal tollCost;

    @NotNull(message = "Custo diário do motorista é obrigatório")
    @DecimalMin(value = "0.0", message = "Custo do motorista não pode ser negativo")
    private BigDecimal driverDailyCost;

    @NotNull(message = "Meta de economia é obrigatória")
    @DecimalMin(value = "0.0", message = "Meta de economia não pode ser negativa")
    private BigDecimal targetSavings;

    @NotNull(message = "Custo máximo por entrega é obrigatório")
    @DecimalMin(value = "0.0", message = "Custo máximo por entrega não pode ser negativo")
    private BigDecimal maxCostPerDelivery;

    public FinancialSettingsDTO() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
}
