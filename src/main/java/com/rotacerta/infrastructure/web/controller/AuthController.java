package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.AuthResponseDTO;
import com.rotacerta.application.dto.CompanyRegistrationDTO;
import com.rotacerta.application.dto.LoginRequestDTO;
import com.rotacerta.application.usecase.AuthUseCase;
import com.rotacerta.application.usecase.CompanyRegistrationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints para controle de acesso e registro")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final CompanyRegistrationUseCase registrationUseCase;

    public AuthController(AuthUseCase authUseCase, CompanyRegistrationUseCase registrationUseCase) {
        this.authUseCase = authUseCase;
        this.registrationUseCase = registrationUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login de um usuário")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authUseCase.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Registra uma nova empresa e seu administrador")
    public ResponseEntity<Void> register(@RequestBody @Valid CompanyRegistrationDTO request) {
        registrationUseCase.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
