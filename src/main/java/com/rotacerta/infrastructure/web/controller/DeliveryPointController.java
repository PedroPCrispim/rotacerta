package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.DeliveryPointDTO;
import com.rotacerta.application.usecase.DeliveryPointUseCase;
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
@RequestMapping("/api/v1/delivery-points")
@Tag(name = "Pontos de Entrega", description = "Gestão de pontos de visita técnica ou coleta")
@SecurityRequirement(name = "bearerAuth")
public class DeliveryPointController {

    private final DeliveryPointUseCase deliveryPointUseCase;

    public DeliveryPointController(DeliveryPointUseCase deliveryPointUseCase) {
        this.deliveryPointUseCase = deliveryPointUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo ponto de entrega")
    public ResponseEntity<DeliveryPointDTO> create(@RequestBody @Valid DeliveryPointDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryPointUseCase.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ponto de entrega existente")
    public ResponseEntity<DeliveryPointDTO> update(@PathVariable UUID id, @RequestBody @Valid DeliveryPointDTO dto) {
        return ResponseEntity.ok(deliveryPointUseCase.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto de entrega por ID")
    public ResponseEntity<DeliveryPointDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(deliveryPointUseCase.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os pontos de entrega da empresa")
    public ResponseEntity<List<DeliveryPointDTO>> findAll() {
        return ResponseEntity.ok(deliveryPointUseCase.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um ponto de entrega")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deliveryPointUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
