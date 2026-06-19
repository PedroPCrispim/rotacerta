package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.DashboardDTO;
import com.rotacerta.domain.model.AnalyticsDailyAggregator;
import com.rotacerta.domain.repository.AnalyticsDailyAggregatorRepository;
import com.rotacerta.domain.repository.AnalyticsRouteSummaryRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsUseCaseImplTest {

    @Mock
    private AnalyticsRouteSummaryRepository routeSummaryRepository;

    @Mock
    private AnalyticsDailyAggregatorRepository dailyAggregatorRepository;

    @InjectMocks
    private AnalyticsUseCaseImpl analyticsUseCase;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);
    }

    @Test
    void shouldGetDashboardKPIs() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        AnalyticsDailyAggregator aggregator = AnalyticsDailyAggregator.builder()
                .kmSaved(100.0)
                .timeSaved(3600L)
                .costSaved(new BigDecimal("500.00"))
                .deliveriesCount(50)
                .build();

        when(dailyAggregatorRepository.findByTenantIdAndDateRange(eq(tenantId), eq(start), eq(end)))
                .thenReturn(List.of(aggregator));
        
        when(routeSummaryRepository.findByTenantIdAndDateRange(eq(tenantId), any(), any()))
                .thenReturn(Collections.emptyList());

        DashboardDTO result = analyticsUseCase.getDashboard(start, end);

        assertEquals(100.0, result.getTotalKmSaved());
        assertEquals(3600L, result.getTotalTimeSaved());
        assertEquals(new BigDecimal("500.00"), result.getTotalCostSaved());
        assertEquals(50, result.getTotalDeliveries());
    }
}
