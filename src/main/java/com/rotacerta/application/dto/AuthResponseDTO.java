package com.rotacerta.application.dto;

import java.util.UUID;

public class AuthResponseDTO {
    private String token;
    private String name;
    private String email;
    private String role;
    private UUID companyId;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String name, String email, String role, UUID companyId) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.companyId = companyId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String name;
        private String email;
        private String role;
        private UUID companyId;

        public Builder token(String token) { this.token = token; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder companyId(UUID companyId) { this.companyId = companyId; return this; }

        public AuthResponseDTO build() {
            return new AuthResponseDTO(token, name, email, role, companyId);
        }
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }
}
