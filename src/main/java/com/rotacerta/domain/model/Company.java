package com.rotacerta.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
    private UUID id;
    private String name;
    private String cnpj;
    private boolean active;
    private LocalDateTime createdAt;

    public Company() {}

    public Company(UUID id, String name, String cnpj, boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String cnpj;
        private boolean active;
        private LocalDateTime createdAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder cnpj(String cnpj) { this.cnpj = cnpj; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Company build() {
            return new Company(id, name, cnpj, active, createdAt);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
