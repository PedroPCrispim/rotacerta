package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.FinancialSettings;

import java.util.Optional;
import java.util.UUID;

public interface FinancialSettingsRepository {
    FinancialSettings save(FinancialSettings settings);
    Optional<FinancialSettings> findByCompanyId(UUID companyId);
}
