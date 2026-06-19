package com.rotacerta.infrastructure.persistence.repository;

import com.rotacerta.infrastructure.persistence.entity.AnalyticsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaAnalyticsEventRepository extends JpaRepository<AnalyticsEventEntity, UUID> {
}
