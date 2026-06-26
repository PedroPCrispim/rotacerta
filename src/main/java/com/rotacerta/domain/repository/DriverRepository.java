package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.Driver;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DriverRepository {
    Driver save(Driver driver);
    Optional<Driver> findById(UUID id);
    List<Driver> findAllByCompanyId(UUID companyId);
    void deleteById(UUID id);
}
