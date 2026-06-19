package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.DeliveryPoint;
import com.rotacerta.domain.repository.DeliveryPointRepository;
import com.rotacerta.infrastructure.persistence.entity.DeliveryPointEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaDeliveryPointRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DeliveryPointRepositoryImpl implements DeliveryPointRepository {

    private final JpaDeliveryPointRepository jpaDeliveryPointRepository;

    public DeliveryPointRepositoryImpl(JpaDeliveryPointRepository jpaDeliveryPointRepository) {
        this.jpaDeliveryPointRepository = jpaDeliveryPointRepository;
    }

    @Override
    public DeliveryPoint save(DeliveryPoint deliveryPoint) {
        DeliveryPointEntity entity = DeliveryPointEntity.builder()
                .id(deliveryPoint.getId())
                .companyId(deliveryPoint.getCompanyId())
                .addressId(deliveryPoint.getAddressId())
                .description(deliveryPoint.getDescription())
                .contactName(deliveryPoint.getContactName())
                .priority(deliveryPoint.getPriority())
                .startTime(deliveryPoint.getStartTime())
                .endTime(deliveryPoint.getEndTime())
                .build();
        entity = jpaDeliveryPointRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<DeliveryPoint> findById(UUID id) {
        return jpaDeliveryPointRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<DeliveryPoint> findAllByCompanyId(UUID companyId) {
        return jpaDeliveryPointRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaDeliveryPointRepository.deleteById(id);
    }

    private DeliveryPoint mapToDomain(DeliveryPointEntity entity) {
        return DeliveryPoint.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .addressId(entity.getAddressId())
                .description(entity.getDescription())
                .contactName(entity.getContactName())
                .priority(entity.getPriority())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .build();
    }
}
