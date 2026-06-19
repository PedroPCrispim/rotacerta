package com.rotacerta.application.dto;

import java.util.UUID;

public class VehiclePerformanceDTO {
    private UUID vehicleId;
    private Long totalDistance;
    private Double avgCapacityUtilization;

    public VehiclePerformanceDTO() {}

    public VehiclePerformanceDTO(UUID vehicleId, Long totalDistance, Double avgCapacityUtilization) {
        this.vehicleId = vehicleId;
        this.totalDistance = totalDistance;
        this.avgCapacityUtilization = avgCapacityUtilization;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID vehicleId;
        private Long totalDistance;
        private Double avgCapacityUtilization;

        public Builder vehicleId(UUID vehicleId) { this.vehicleId = vehicleId; return this; }
        public Builder totalDistance(Long totalDistance) { this.totalDistance = totalDistance; return this; }
        public Builder avgCapacityUtilization(Double avgCapacityUtilization) { this.avgCapacityUtilization = avgCapacityUtilization; return this; }

        public VehiclePerformanceDTO build() {
            return new VehiclePerformanceDTO(vehicleId, totalDistance, avgCapacityUtilization);
        }
    }

    public UUID getVehicleId() { return vehicleId; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }
    public Long getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Long totalDistance) { this.totalDistance = totalDistance; }
    public Double getAvgCapacityUtilization() { return avgCapacityUtilization; }
    public void setAvgCapacityUtilization(Double avgCapacityUtilization) { this.avgCapacityUtilization = avgCapacityUtilization; }
}
