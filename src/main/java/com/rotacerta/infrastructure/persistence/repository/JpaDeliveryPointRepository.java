package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.DeliveryPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface JpaDeliveryPointRepository extends JpaRepository<DeliveryPointEntity, UUID> {
    List<DeliveryPointEntity> findAllByCompanyId(UUID companyId);
}
