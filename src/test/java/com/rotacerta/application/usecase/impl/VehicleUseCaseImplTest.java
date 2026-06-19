package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.VehicleDTO;
import com.rotacerta.domain.exception.EntityNotFoundException;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleUseCaseImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleUseCaseImpl vehicleUseCase;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.setTenantId(tenantId);
    }

    @Test
    void shouldCreateVehicle() {
        VehicleDTO dto = new VehicleDTO();
        dto.setPlate("ABC-1234");
        dto.setModel("Fiorino");
        dto.setCapacity(500.0);
        dto.setFuelType("FLEX");
        dto.setAvgConsumption(10.0);

        Vehicle vehicle = Vehicle.builder()
                .id(UUID.randomUUID())
                .companyId(tenantId)
                .plate(dto.getPlate())
                .model(dto.getModel())
                .build();

        when(vehicleRepository.save(any())).thenReturn(vehicle);

        VehicleDTO result = vehicleUseCase.create(dto);

        assertNotNull(result.getId());
        assertEquals(dto.getPlate(), result.getPlate());
        verify(vehicleRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleUseCase.findById(id));
    }
}
