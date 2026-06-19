package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.DashboardDTO;
import com.rotacerta.application.dto.AnalyticsTimelineDTO;
import com.rotacerta.application.dto.FinancialAnalyticsDTO;
import com.rotacerta.application.dto.FleetAnalyticsDTO;
import com.rotacerta.application.dto.OperationsAnalyticsDTO;
import com.rotacerta.application.usecase.AnalyticsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics", description = "Endpoints para KPIs e inteligência de negócio")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final AnalyticsUseCase analyticsUseCase;

    public AnalyticsController(AnalyticsUseCase analyticsUseCase) {
        this.analyticsUseCase = analyticsUseCase;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Resumo geral dos KPIs para o dashboard principal")
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(analyticsUseCase.getDashboard(start, end));
    }

    @GetMapping("/timeline")
    @Operation(summary = "Serie temporal comparativa dos indicadores do dashboard")
    public ResponseEntity<AnalyticsTimelineDTO> getTimeline(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "month") String granularity,
            @RequestParam(defaultValue = "costSaved") String metric,
            @RequestParam(required = false) UUID vehicleId) {
        return ResponseEntity.ok(analyticsUseCase.getTimeline(start, end, granularity, metric, vehicleId));
    }

    @GetMapping("/financial")
    @Operation(summary = "KPIs financeiros e economia")
    public ResponseEntity<FinancialAnalyticsDTO> getFinancial(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(analyticsUseCase.getFinancial(start, end));
    }

    @GetMapping("/fleet")
    @Operation(summary = "Performance da frota e utilização")
    public ResponseEntity<FleetAnalyticsDTO> getFleet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(analyticsUseCase.getFleet(start, end));
    }

    @GetMapping("/operations")
    @Operation(summary = "KPIs operacionais e volumetria")
    public ResponseEntity<OperationsAnalyticsDTO> getOperations(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(analyticsUseCase.getOperations(start, end));
    }
}
