package com.rotacerta.application.dto;

import java.util.List;

public class FleetAnalyticsDTO {
    private Integer totalVehicles;
    private Double avgUtilization;
    private List<VehiclePerformanceDTO> vehiclePerformances;

    public FleetAnalyticsDTO() {}

    public FleetAnalyticsDTO(Integer totalVehicles, Double avgUtilization, List<VehiclePerformanceDTO> vehiclePerformances) {
        this.totalVehicles = totalVehicles;
        this.avgUtilization = avgUtilization;
        this.vehiclePerformances = vehiclePerformances;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer totalVehicles;
        private Double avgUtilization;
        private List<VehiclePerformanceDTO> vehiclePerformances;

        public Builder totalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; return this; }
        public Builder avgUtilization(Double avgUtilization) { this.avgUtilization = avgUtilization; return this; }
        public Builder vehiclePerformances(List<VehiclePerformanceDTO> vehiclePerformances) { this.vehiclePerformances = vehiclePerformances; return this; }

        public FleetAnalyticsDTO build() {
            return new FleetAnalyticsDTO(totalVehicles, avgUtilization, vehiclePerformances);
        }
    }

    public Integer getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(Integer totalVehicles) { this.totalVehicles = totalVehicles; }
    public Double getAvgUtilization() { return avgUtilization; }
    public void setAvgUtilization(Double avgUtilization) { this.avgUtilization = avgUtilization; }
    public List<VehiclePerformanceDTO> getVehiclePerformances() { return vehiclePerformances; }
    public void setVehiclePerformances(List<VehiclePerformanceDTO> vehiclePerformances) { this.vehiclePerformances = vehiclePerformances; }
}
