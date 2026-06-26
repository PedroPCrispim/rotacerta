package com.rotacerta.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Driver {
    private UUID id;
    private UUID companyId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private String status;
    private UUID assignedVehicleId;
    private UUID nextStopAddressId;
    private String currentRouteLabel;
    private String routeExecutionStatus;
    private Boolean checkInRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Driver() {
    }

    public Driver(UUID id, UUID companyId, String name, String email, String phone, String licenseNumber,
                  String status, UUID assignedVehicleId, UUID nextStopAddressId, String currentRouteLabel,
                  String routeExecutionStatus, Boolean checkInRequired, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.status = status;
        this.assignedVehicleId = assignedVehicleId;
        this.nextStopAddressId = nextStopAddressId;
        this.currentRouteLabel = currentRouteLabel;
        this.routeExecutionStatus = routeExecutionStatus;
        this.checkInRequired = checkInRequired;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID companyId;
        private String name;
        private String email;
        private String phone;
        private String licenseNumber;
        private String status;
        private UUID assignedVehicleId;
        private UUID nextStopAddressId;
        private String currentRouteLabel;
        private String routeExecutionStatus;
        private Boolean checkInRequired;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder licenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder assignedVehicleId(UUID assignedVehicleId) { this.assignedVehicleId = assignedVehicleId; return this; }
        public Builder nextStopAddressId(UUID nextStopAddressId) { this.nextStopAddressId = nextStopAddressId; return this; }
        public Builder currentRouteLabel(String currentRouteLabel) { this.currentRouteLabel = currentRouteLabel; return this; }
        public Builder routeExecutionStatus(String routeExecutionStatus) { this.routeExecutionStatus = routeExecutionStatus; return this; }
        public Builder checkInRequired(Boolean checkInRequired) { this.checkInRequired = checkInRequired; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Driver build() {
            return new Driver(id, companyId, name, email, phone, licenseNumber, status, assignedVehicleId, nextStopAddressId,
                    currentRouteLabel, routeExecutionStatus, checkInRequired, createdAt, updatedAt);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
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
    public UUID getNextStopAddressId() { return nextStopAddressId; }
    public void setNextStopAddressId(UUID nextStopAddressId) { this.nextStopAddressId = nextStopAddressId; }
    public String getCurrentRouteLabel() { return currentRouteLabel; }
    public void setCurrentRouteLabel(String currentRouteLabel) { this.currentRouteLabel = currentRouteLabel; }
    public String getRouteExecutionStatus() { return routeExecutionStatus; }
    public void setRouteExecutionStatus(String routeExecutionStatus) { this.routeExecutionStatus = routeExecutionStatus; }
    public Boolean getCheckInRequired() { return checkInRequired; }
    public void setCheckInRequired(Boolean checkInRequired) { this.checkInRequired = checkInRequired; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
