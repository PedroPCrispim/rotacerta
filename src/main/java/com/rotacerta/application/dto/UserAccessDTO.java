package com.rotacerta.application.dto;

public class UserAccessDTO {
    private String name;
    private String email;
    private String role;
    private DashboardViewAccessDTO viewAccess;

    public UserAccessDTO() {}

    public UserAccessDTO(String name, String email, String role, DashboardViewAccessDTO viewAccess) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.viewAccess = viewAccess;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String email;
        private String role;
        private DashboardViewAccessDTO viewAccess;

        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder viewAccess(DashboardViewAccessDTO viewAccess) { this.viewAccess = viewAccess; return this; }

        public UserAccessDTO build() {
            return new UserAccessDTO(name, email, role, viewAccess);
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public DashboardViewAccessDTO getViewAccess() { return viewAccess; }
    public void setViewAccess(DashboardViewAccessDTO viewAccess) { this.viewAccess = viewAccess; }
}
