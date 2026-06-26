package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.FinancialSettings;
import com.rotacerta.domain.repository.FinancialSettingsRepository;
import com.rotacerta.infrastructure.persistence.entity.FinancialSettingsEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaFinancialSettingsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class FinancialSettingsRepositoryImpl implements FinancialSettingsRepository {
    private final JpaFinancialSettingsRepository jpaFinancialSettingsRepository;

    public FinancialSettingsRepositoryImpl(JpaFinancialSettingsRepository jpaFinancialSettingsRepository) {
        this.jpaFinancialSettingsRepository = jpaFinancialSettingsRepository;
    }

    @Override
    public FinancialSettings save(FinancialSettings settings) {
        FinancialSettingsEntity entity = jpaFinancialSettingsRepository.findByCompanyId(settings.getCompanyId())
                .orElseGet(FinancialSettingsEntity::new);

        entity.setId(settings.getId());
        entity.setCompanyId(settings.getCompanyId());
        entity.setRegion(settings.getRegion());
        entity.setGasolinePrice(settings.getGasolinePrice());
        entity.setEthanolPrice(settings.getEthanolPrice());
        entity.setDieselPrice(settings.getDieselPrice());
        entity.setFixedCostPerVehicle(settings.getFixedCostPerVehicle());
        entity.setMaintenanceReserve(settings.getMaintenanceReserve());
        entity.setTollCost(settings.getTollCost());
        entity.setDriverDailyCost(settings.getDriverDailyCost());
        entity.setTargetSavings(settings.getTargetSavings());
        entity.setMaxCostPerDelivery(settings.getMaxCostPerDelivery());

        return mapToDomain(jpaFinancialSettingsRepository.save(entity));
    }

    @Override
    public Optional<FinancialSettings> findByCompanyId(UUID companyId) {
        return jpaFinancialSettingsRepository.findByCompanyId(companyId).map(this::mapToDomain);
    }

    private FinancialSettings mapToDomain(FinancialSettingsEntity entity) {
        return FinancialSettings.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .region(entity.getRegion())
                .gasolinePrice(entity.getGasolinePrice())
                .ethanolPrice(entity.getEthanolPrice())
                .dieselPrice(entity.getDieselPrice())
                .fixedCostPerVehicle(entity.getFixedCostPerVehicle())
                .maintenanceReserve(entity.getMaintenanceReserve())
                .tollCost(entity.getTollCost())
                .driverDailyCost(entity.getDriverDailyCost())
                .targetSavings(entity.getTargetSavings())
                .maxCostPerDelivery(entity.getMaxCostPerDelivery())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
