package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.DashboardDTO;
import com.rotacerta.application.dto.FinancialAnalyticsDTO;
import com.rotacerta.application.dto.FinancialSettingsDTO;
import com.rotacerta.application.dto.FleetAnalyticsDTO;
import com.rotacerta.application.dto.OperationsAnalyticsDTO;
import com.rotacerta.application.dto.AnalyticsTimelineDTO;

import java.time.LocalDate;
import java.util.UUID;

public interface AnalyticsUseCase {
    DashboardDTO getDashboard(LocalDate start, LocalDate end);
    FinancialAnalyticsDTO getFinancial(LocalDate start, LocalDate end);
    FinancialSettingsDTO getFinancialSettings();
    FinancialSettingsDTO saveFinancialSettings(FinancialSettingsDTO dto);
    FleetAnalyticsDTO getFleet(LocalDate start, LocalDate end);
    OperationsAnalyticsDTO getOperations(LocalDate start, LocalDate end);
    AnalyticsTimelineDTO getTimeline(LocalDate start, LocalDate end, String granularity, String metric, UUID vehicleId);
    byte[] exportReport(LocalDate start, LocalDate end, String granularity, String view, UUID vehicleId);
}
