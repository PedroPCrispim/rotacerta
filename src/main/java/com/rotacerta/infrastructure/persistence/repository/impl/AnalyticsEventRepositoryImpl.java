package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.AnalyticsEvent;
import com.rotacerta.domain.repository.AnalyticsEventRepository;
import com.rotacerta.infrastructure.persistence.entity.AnalyticsEventEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaAnalyticsEventRepository;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventRepositoryImpl implements AnalyticsEventRepository {

    private final JpaAnalyticsEventRepository jpaRepository;

    public AnalyticsEventRepositoryImpl(JpaAnalyticsEventRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AnalyticsEvent save(AnalyticsEvent event) {
        AnalyticsEventEntity entity = AnalyticsEventEntity.builder()
                .id(event.getId())
                .tenantId(event.getTenantId())
                .eventType(event.getEventType())
                .payload(event.getPayload())
                .build();
        entity = jpaRepository.save(entity);
        return AnalyticsEvent.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .eventType(entity.getEventType())
                .payload(entity.getPayload())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
