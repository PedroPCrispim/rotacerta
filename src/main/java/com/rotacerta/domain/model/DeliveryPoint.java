package com.rotacerta.domain.model;

import java.time.LocalTime;
import java.util.UUID;

public class DeliveryPoint {
    private UUID id;
    private UUID companyId;
    private UUID addressId;
    private String description;
    private String contactName;
    private Integer priority; // 1 (High) to 5 (Low)
    private LocalTime startTime;
    private LocalTime endTime;

    public DeliveryPoint() {}

    public DeliveryPoint(UUID id, UUID companyId, UUID addressId, String description, String contactName, Integer priority, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.companyId = companyId;
        this.addressId = addressId;
        this.description = description;
        this.contactName = contactName;
        this.priority = priority;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID companyId;
        private UUID addressId;
        private String description;
        private String contactName;
        private Integer priority;
        private LocalTime startTime;
        private LocalTime endTime;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }
        public Builder addressId(UUID addressId) { this.addressId = addressId; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder contactName(String contactName) { this.contactName = contactName; return this; }
        public Builder priority(Integer priority) { this.priority = priority; return this; }
        public Builder startTime(LocalTime startTime) { this.startTime = startTime; return this; }
        public Builder endTime(LocalTime endTime) { this.endTime = endTime; return this; }

        public DeliveryPoint build() {
            return new DeliveryPoint(id, companyId, addressId, description, contactName, priority, startTime, endTime);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
    public UUID getAddressId() { return addressId; }
    public void setAddressId(UUID addressId) { this.addressId = addressId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
