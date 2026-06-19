package com.rotacerta.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CompanyRegistrationDTO {
    @NotBlank(message = "Nome da empresa é obrigatório")
    private String companyName;

    @NotBlank(message = "CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "Nome do administrador é obrigatório")
    private String adminName;

    @NotBlank(message = "Email do administrador é obrigatório")
    @Email(message = "Email inválido")
    private String adminEmail;

    @NotBlank(message = "Senha é obrigatória")
    private String adminPassword;

    public CompanyRegistrationDTO() {}

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
    public String getAdminPassword() { return adminPassword; }
    public void setAdminPassword(String adminPassword) { this.adminPassword = adminPassword; }
}
