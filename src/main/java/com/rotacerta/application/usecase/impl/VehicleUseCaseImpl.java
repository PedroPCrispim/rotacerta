package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.VehicleDTO;
import com.rotacerta.application.usecase.VehicleUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.exception.EntityNotFoundException;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleUseCaseImpl implements VehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public VehicleUseCaseImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public VehicleDTO create(VehicleDTO dto) {
        Vehicle vehicle = mapToDomain(dto);
        vehicle.setCompanyId(TenantContext.getTenantId());
        vehicle = vehicleRepository.save(vehicle);
        return mapToDTO(vehicle);
    }

    @Override
    public VehicleDTO update(UUID id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        if (!vehicle.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        vehicle.setPlate(dto.getPlate());
        vehicle.setModel(dto.getModel());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setAvgConsumption(dto.getAvgConsumption());
        vehicle.setCostPerKm(dto.getCostPerKm());

        vehicle = vehicleRepository.save(vehicle);
        return mapToDTO(vehicle);
    }

    @Override
    public VehicleDTO findById(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        if (!vehicle.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        return mapToDTO(vehicle);
    }

    @Override
    public List<VehicleDTO> findAll() {
        return vehicleRepository.findAllByCompanyId(TenantContext.getTenantId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));

        if (!vehicle.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        vehicleRepository.deleteById(id);
    }

    private Vehicle mapToDomain(VehicleDTO dto) {
        return Vehicle.builder()
                .id(dto.getId())
                .plate(dto.getPlate())
                .model(dto.getModel())
                .capacity(dto.getCapacity())
                .fuelType(dto.getFuelType())
                .avgConsumption(dto.getAvgConsumption())
                .costPerKm(dto.getCostPerKm())
                .build();
    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setPlate(vehicle.getPlate());
        dto.setModel(vehicle.getModel());
        dto.setCapacity(vehicle.getCapacity());
        dto.setFuelType(vehicle.getFuelType());
        dto.setAvgConsumption(vehicle.getAvgConsumption());
        dto.setCostPerKm(vehicle.getCostPerKm());
        return dto;
    }
}
