package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.AnalyticsEvent;
import java.util.UUID;

public interface AnalyticsEventRepository {
    AnalyticsEvent save(AnalyticsEvent event);
}
