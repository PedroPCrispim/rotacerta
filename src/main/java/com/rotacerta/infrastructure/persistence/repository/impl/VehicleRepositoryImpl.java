package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.persistence.entity.VehicleEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaVehicleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class VehicleRepositoryImpl implements VehicleRepository {

    private final JpaVehicleRepository jpaVehicleRepository;

    public VehicleRepositoryImpl(JpaVehicleRepository jpaVehicleRepository) {
        this.jpaVehicleRepository = jpaVehicleRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = VehicleEntity.builder()
                .id(vehicle.getId())
                .companyId(vehicle.getCompanyId())
                .plate(vehicle.getPlate())
                .model(vehicle.getModel())
                .capacity(vehicle.getCapacity())
                .fuelType(vehicle.getFuelType())
                .avgConsumption(vehicle.getAvgConsumption())
                .costPerKm(vehicle.getCostPerKm())
                .build();
        entity = jpaVehicleRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaVehicleRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Vehicle> findAllByCompanyId(UUID companyId) {
        return jpaVehicleRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaVehicleRepository.deleteById(id);
    }

    private Vehicle mapToDomain(VehicleEntity entity) {
        return Vehicle.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .plate(entity.getPlate())
                .model(entity.getModel())
                .capacity(entity.getCapacity())
                .fuelType(entity.getFuelType())
                .avgConsumption(entity.getAvgConsumption())
                .costPerKm(entity.getCostPerKm())
                .build();
    }
}
