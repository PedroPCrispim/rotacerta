package com.rotacerta.application.dto;

import java.util.UUID;

public class DriverPortalDTO {
    private UUID driverId;
    private String driverName;
    private String status;
    private String phone;
    private String assignedVehicleLabel;
    private UUID assignedVehicleId;
    private UUID nextStopAddressId;
    private String nextStopLabel;
    private String nextStopFullAddress;
    private Double nextStopLatitude;
    private Double nextStopLongitude;
    private String currentRouteLabel;
    private String routeExecutionStatus;
    private Boolean checkInRequired;

    public DriverPortalDTO() {
    }

    public UUID getDriverId() { return driverId; }
    public void setDriverId(UUID driverId) { this.driverId = driverId; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAssignedVehicleLabel() { return assignedVehicleLabel; }
    public void setAssignedVehicleLabel(String assignedVehicleLabel) { this.assignedVehicleLabel = assignedVehicleLabel; }
    public UUID getAssignedVehicleId() { return assignedVehicleId; }
    public void setAssignedVehicleId(UUID assignedVehicleId) { this.assignedVehicleId = assignedVehicleId; }
    public UUID getNextStopAddressId() { return nextStopAddressId; }
    public void setNextStopAddressId(UUID nextStopAddressId) { this.nextStopAddressId = nextStopAddressId; }
    public String getNextStopLabel() { return nextStopLabel; }
    public void setNextStopLabel(String nextStopLabel) { this.nextStopLabel = nextStopLabel; }
    public String getNextStopFullAddress() { return nextStopFullAddress; }
    public void setNextStopFullAddress(String nextStopFullAddress) { this.nextStopFullAddress = nextStopFullAddress; }
    public Double getNextStopLatitude() { return nextStopLatitude; }
    public void setNextStopLatitude(Double nextStopLatitude) { this.nextStopLatitude = nextStopLatitude; }
    public Double getNextStopLongitude() { return nextStopLongitude; }
    public void setNextStopLongitude(Double nextStopLongitude) { this.nextStopLongitude = nextStopLongitude; }
    public String getCurrentRouteLabel() { return currentRouteLabel; }
    public void setCurrentRouteLabel(String currentRouteLabel) { this.currentRouteLabel = currentRouteLabel; }
    public String getRouteExecutionStatus() { return routeExecutionStatus; }
    public void setRouteExecutionStatus(String routeExecutionStatus) { this.routeExecutionStatus = routeExecutionStatus; }
    public Boolean getCheckInRequired() { return checkInRequired; }
    public void setCheckInRequired(Boolean checkInRequired) { this.checkInRequired = checkInRequired; }
}
