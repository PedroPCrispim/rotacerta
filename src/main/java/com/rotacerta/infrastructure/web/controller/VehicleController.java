package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.VehicleDTO;
import com.rotacerta.application.usecase.VehicleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Veículos", description = "CRUD de veículos da frota")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleUseCase vehicleUseCase;

    public VehicleController(VehicleUseCase vehicleUseCase) {
        this.vehicleUseCase = vehicleUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo veículo")
    public ResponseEntity<VehicleDTO> create(@RequestBody @Valid VehicleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleUseCase.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veículo existente")
    public ResponseEntity<VehicleDTO> update(@PathVariable UUID id, @RequestBody @Valid VehicleDTO dto) {
        return ResponseEntity.ok(vehicleUseCase.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um veículo por ID")
    public ResponseEntity<VehicleDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(vehicleUseCase.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os veículos da empresa")
    public ResponseEntity<List<VehicleDTO>> findAll() {
        return ResponseEntity.ok(vehicleUseCase.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um veículo")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vehicleUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
