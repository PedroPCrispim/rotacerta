package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.VehicleDTO;
import java.util.List;
import java.util.UUID;

public interface VehicleUseCase {
    VehicleDTO create(VehicleDTO dto);
    VehicleDTO update(UUID id, VehicleDTO dto);
    VehicleDTO findById(UUID id);
    List<VehicleDTO> findAll();
    void delete(UUID id);
}
