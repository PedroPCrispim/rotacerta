package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.Driver;
import com.rotacerta.domain.repository.DriverRepository;
import com.rotacerta.infrastructure.persistence.entity.DriverEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaDriverRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DriverRepositoryImpl implements DriverRepository {
    private final JpaDriverRepository jpaDriverRepository;

    public DriverRepositoryImpl(JpaDriverRepository jpaDriverRepository) {
        this.jpaDriverRepository = jpaDriverRepository;
    }

    @Override
    public Driver save(Driver driver) {
        DriverEntity entity = driver.getId() == null
                ? new DriverEntity()
                : jpaDriverRepository.findById(driver.getId()).orElseGet(DriverEntity::new);
        entity.setId(driver.getId());
        entity.setCompanyId(driver.getCompanyId());
        entity.setName(driver.getName());
        entity.setEmail(driver.getEmail());
        entity.setPhone(driver.getPhone());
        entity.setLicenseNumber(driver.getLicenseNumber());
        entity.setStatus(driver.getStatus());
        entity.setAssignedVehicleId(driver.getAssignedVehicleId());
        entity.setNextStopAddressId(driver.getNextStopAddressId());
        entity.setCurrentRouteLabel(driver.getCurrentRouteLabel());
        entity.setRouteExecutionStatus(driver.getRouteExecutionStatus());
        entity.setCheckInRequired(Boolean.TRUE.equals(driver.getCheckInRequired()));
        entity = jpaDriverRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<Driver> findById(UUID id) {
        return jpaDriverRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Driver> findAllByCompanyId(UUID companyId) {
        return jpaDriverRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaDriverRepository.deleteById(id);
    }

    private Driver mapToDomain(DriverEntity entity) {
        return Driver.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .licenseNumber(entity.getLicenseNumber())
                .status(entity.getStatus())
                .assignedVehicleId(entity.getAssignedVehicleId())
                .nextStopAddressId(entity.getNextStopAddressId())
                .currentRouteLabel(entity.getCurrentRouteLabel())
                .routeExecutionStatus(entity.getRouteExecutionStatus())
                .checkInRequired(entity.getCheckInRequired())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
