package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.DeliveryPointDTO;
import java.util.List;
import java.util.UUID;

public interface DeliveryPointUseCase {
    DeliveryPointDTO create(DeliveryPointDTO dto);
    DeliveryPointDTO update(UUID id, DeliveryPointDTO dto);
    DeliveryPointDTO findById(UUID id);
    List<DeliveryPointDTO> findAll();
    void delete(UUID id);
}
