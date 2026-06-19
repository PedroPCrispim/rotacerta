package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.RoutingRequestDTO;
import com.rotacerta.application.dto.RoutingResultDTO;
import com.rotacerta.application.usecase.RoutingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routing")
@Tag(name = "Roteirização", description = "Motor de otimização de rotas")
@SecurityRequirement(name = "bearerAuth")
public class RoutingController {

    private final RoutingUseCase routingUseCase;

    public RoutingController(RoutingUseCase routingUseCase) {
        this.routingUseCase = routingUseCase;
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calcula a melhor rota para a frota e pontos de entrega atuais")
    public ResponseEntity<RoutingResultDTO> calculate(@RequestBody RoutingRequestDTO request) {
        return ResponseEntity.ok(routingUseCase.calculateRoute(request));
    }
}
