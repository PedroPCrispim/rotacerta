package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.DriverDTO;
import com.rotacerta.application.dto.DriverPortalDTO;
import com.rotacerta.application.usecase.DriverUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Motoristas", description = "CRUD de motoristas e base do portal mobile")
@SecurityRequirement(name = "bearerAuth")
public class DriverController {
    private final DriverUseCase driverUseCase;

    public DriverController(DriverUseCase driverUseCase) {
        this.driverUseCase = driverUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo motorista")
    public ResponseEntity<DriverDTO> create(@RequestBody @Valid DriverDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverUseCase.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um motorista")
    public ResponseEntity<DriverDTO> update(@PathVariable UUID id, @RequestBody @Valid DriverDTO dto) {
        return ResponseEntity.ok(driverUseCase.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um motorista por ID")
    public ResponseEntity<DriverDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(driverUseCase.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os motoristas da empresa")
    public ResponseEntity<List<DriverDTO>> findAll() {
        return ResponseEntity.ok(driverUseCase.findAll());
    }

    @GetMapping("/{id}/portal")
    @Operation(summary = "Retorna o resumo mobile do portal do motorista")
    public ResponseEntity<DriverPortalDTO> getPortal(@PathVariable UUID id) {
        return ResponseEntity.ok(driverUseCase.getPortalById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um motorista")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        driverUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
