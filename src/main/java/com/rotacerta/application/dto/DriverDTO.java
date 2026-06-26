package com.rotacerta.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class DriverDTO {
    private UUID id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    private String phone;

    @NotBlank(message = "CNH é obrigatória")
    private String licenseNumber;

    @NotBlank(message = "Status é obrigatório")
    private String status;

    private UUID assignedVehicleId;
    private String assignedVehicleLabel;
    private UUID nextStopAddressId;
    private String nextStopLabel;
    private String currentRouteLabel;
    private String routeExecutionStatus;
    private Boolean checkInRequired;

    public DriverDTO() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public UUID getAssignedVehicleId() { return assignedVehicleId; }
    public void setAssignedVehicleId(UUID assignedVehicleId) { this.assignedVehicleId = assignedVehicleId; }
    public String getAssignedVehicleLabel() { return assignedVehicleLabel; }
    public void setAssignedVehicleLabel(String assignedVehicleLabel) { this.assignedVehicleLabel = assignedVehicleLabel; }
    public UUID getNextStopAddressId() { return nextStopAddressId; }
    public void setNextStopAddressId(UUID nextStopAddressId) { this.nextStopAddressId = nextStopAddressId; }
    public String getNextStopLabel() { return nextStopLabel; }
    public void setNextStopLabel(String nextStopLabel) { this.nextStopLabel = nextStopLabel; }
    public String getCurrentRouteLabel() { return currentRouteLabel; }
    public void setCurrentRouteLabel(String currentRouteLabel) { this.currentRouteLabel = currentRouteLabel; }
    public String getRouteExecutionStatus() { return routeExecutionStatus; }
    public void setRouteExecutionStatus(String routeExecutionStatus) { this.routeExecutionStatus = routeExecutionStatus; }
    public Boolean getCheckInRequired() { return checkInRequired; }
    public void setCheckInRequired(Boolean checkInRequired) { this.checkInRequired = checkInRequired; }
}
