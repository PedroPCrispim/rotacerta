package com.rotacerta.application.dto;

import java.util.List;
import java.util.UUID;

public class VehicleRouteDTO {
    private UUID vehicleId;
    private String vehiclePlate;
    private List<DeliveryPointDTO> visits;
    private long distance;
    private String googleMapsUrl;
    private String wazeUrl;

    public VehicleRouteDTO() {}

    public VehicleRouteDTO(UUID vehicleId, String vehiclePlate, List<DeliveryPointDTO> visits, long distance, String googleMapsUrl, String wazeUrl) {
        this.vehicleId = vehicleId;
        this.vehiclePlate = vehiclePlate;
        this.visits = visits;
        this.distance = distance;
        this.googleMapsUrl = googleMapsUrl;
        this.wazeUrl = wazeUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID vehicleId;
        private String vehiclePlate;
        private List<DeliveryPointDTO> visits;
        private long distance;
        private String googleMapsUrl;
        private String wazeUrl;

        public Builder vehicleId(UUID vehicleId) { this.vehicleId = vehicleId; return this; }
        public Builder vehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; return this; }
        public Builder visits(List<DeliveryPointDTO> visits) { this.visits = visits; return this; }
        public Builder distance(long distance) { this.distance = distance; return this; }
        public Builder googleMapsUrl(String googleMapsUrl) { this.googleMapsUrl = googleMapsUrl; return this; }
        public Builder wazeUrl(String wazeUrl) { this.wazeUrl = wazeUrl; return this; }

        public VehicleRouteDTO build() {
            return new VehicleRouteDTO(vehicleId, vehiclePlate, visits, distance, googleMapsUrl, wazeUrl);
        }
    }

    public UUID getVehicleId() { return vehicleId; }
    public void setVehicleId(UUID vehicleId) { this.vehicleId = vehicleId; }
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public List<DeliveryPointDTO> getVisits() { return visits; }
    public void setVisits(List<DeliveryPointDTO> visits) { this.visits = visits; }
    public long getDistance() { return distance; }
    public void setDistance(long distance) { this.distance = distance; }
    public String getGoogleMapsUrl() { return googleMapsUrl; }
    public void setGoogleMapsUrl(String googleMapsUrl) { this.googleMapsUrl = googleMapsUrl; }
    public String getWazeUrl() { return wazeUrl; }
    public void setWazeUrl(String wazeUrl) { this.wazeUrl = wazeUrl; }
}
