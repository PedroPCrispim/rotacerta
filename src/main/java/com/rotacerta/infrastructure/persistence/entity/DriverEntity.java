package com.rotacerta.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "drivers")
public class DriverEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String status;

    @Column(name = "assigned_vehicle_id")
    private UUID assignedVehicleId;

    @Column(name = "next_stop_address_id")
    private UUID nextStopAddressId;

    @Column(name = "current_route_label")
    private String currentRouteLabel;

    @Column(name = "route_execution_status")
    private String routeExecutionStatus;

    @Column(name = "check_in_required", nullable = false)
    private Boolean checkInRequired;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public DriverEntity() {
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
