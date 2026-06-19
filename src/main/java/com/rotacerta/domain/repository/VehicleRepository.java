package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.Vehicle;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(UUID id);
    List<Vehicle> findAllByCompanyId(UUID companyId);
    void deleteById(UUID id);
}
