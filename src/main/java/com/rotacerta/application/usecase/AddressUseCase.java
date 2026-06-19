package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.AddressDTO;
import java.util.List;
import java.util.UUID;

public interface AddressUseCase {
    AddressDTO create(AddressDTO dto);
    AddressDTO update(UUID id, AddressDTO dto);
    AddressDTO findById(UUID id);
    List<AddressDTO> findAll();
    void delete(UUID id);
}
