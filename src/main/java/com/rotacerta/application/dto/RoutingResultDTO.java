package com.rotacerta.application.dto;

import java.util.List;

public class RoutingResultDTO {
    private List<VehicleRouteDTO> routes;
    private long totalDistance;
    private long totalEstimatedTime; // em segundos

    public RoutingResultDTO() {}

    public RoutingResultDTO(List<VehicleRouteDTO> routes, long totalDistance, long totalEstimatedTime) {
        this.routes = routes;
        this.totalDistance = totalDistance;
        this.totalEstimatedTime = totalEstimatedTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<VehicleRouteDTO> routes;
        private long totalDistance;
        private long totalEstimatedTime;

        public Builder routes(List<VehicleRouteDTO> routes) { this.routes = routes; return this; }
        public Builder totalDistance(long totalDistance) { this.totalDistance = totalDistance; return this; }
        public Builder totalEstimatedTime(long totalEstimatedTime) { this.totalEstimatedTime = totalEstimatedTime; return this; }

        public RoutingResultDTO build() {
            return new RoutingResultDTO(routes, totalDistance, totalEstimatedTime);
        }
    }

    public List<VehicleRouteDTO> getRoutes() { return routes; }
    public void setRoutes(List<VehicleRouteDTO> routes) { this.routes = routes; }
    public long getTotalDistance() { return totalDistance; }
    public void setTotalDistance(long totalDistance) { this.totalDistance = totalDistance; }
    public long getTotalEstimatedTime() { return totalEstimatedTime; }
    public void setTotalEstimatedTime(long totalEstimatedTime) { this.totalEstimatedTime = totalEstimatedTime; }
}
