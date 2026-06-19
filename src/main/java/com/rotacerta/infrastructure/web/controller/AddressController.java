package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.AddressDTO;
import com.rotacerta.application.usecase.AddressUseCase;
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
@RequestMapping("/api/v1/addresses")
@Tag(name = "Endereços", description = "Gestão de endereços de clientes e bases")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressUseCase addressUseCase;

    public AddressController(AddressUseCase addressUseCase) {
        this.addressUseCase = addressUseCase;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo endereço")
    public ResponseEntity<AddressDTO> create(@RequestBody @Valid AddressDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressUseCase.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um endereço existente")
    public ResponseEntity<AddressDTO> update(@PathVariable UUID id, @RequestBody @Valid AddressDTO dto) {
        return ResponseEntity.ok(addressUseCase.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um endereço por ID")
    public ResponseEntity<AddressDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressUseCase.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os endereços da empresa")
    public ResponseEntity<List<AddressDTO>> findAll() {
        return ResponseEntity.ok(addressUseCase.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um endereço")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
