package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.DeliveryPoint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryPointRepository {
    DeliveryPoint save(DeliveryPoint deliveryPoint);
    Optional<DeliveryPoint> findById(UUID id);
    List<DeliveryPoint> findAllByCompanyId(UUID companyId);
    void deleteById(UUID id);
}
