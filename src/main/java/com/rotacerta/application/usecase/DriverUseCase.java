package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.DriverDTO;
import com.rotacerta.application.dto.DriverPortalDTO;

import java.util.List;
import java.util.UUID;

public interface DriverUseCase {
    DriverDTO create(DriverDTO dto);
    DriverDTO update(UUID id, DriverDTO dto);
    DriverDTO findById(UUID id);
    List<DriverDTO> findAll();
    DriverPortalDTO getPortalById(UUID id);
    void delete(UUID id);
}
