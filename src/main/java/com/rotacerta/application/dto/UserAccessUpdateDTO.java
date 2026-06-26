package com.rotacerta.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserAccessUpdateDTO {
    @NotBlank(message = "Email do usuario e obrigatorio")
    @Email(message = "Email do usuario deve ser valido")
    private String email;

    @NotNull(message = "As permissoes da dashboard sao obrigatorias")
    @Valid
    private DashboardViewAccessDTO viewAccess;

    public UserAccessUpdateDTO() {}

    public UserAccessUpdateDTO(String email, DashboardViewAccessDTO viewAccess) {
        this.email = email;
        this.viewAccess = viewAccess;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private DashboardViewAccessDTO viewAccess;

        public Builder email(String email) { this.email = email; return this; }
        public Builder viewAccess(DashboardViewAccessDTO viewAccess) { this.viewAccess = viewAccess; return this; }

        public UserAccessUpdateDTO build() {
            return new UserAccessUpdateDTO(email, viewAccess);
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public DashboardViewAccessDTO getViewAccess() { return viewAccess; }
    public void setViewAccess(DashboardViewAccessDTO viewAccess) { this.viewAccess = viewAccess; }
}
