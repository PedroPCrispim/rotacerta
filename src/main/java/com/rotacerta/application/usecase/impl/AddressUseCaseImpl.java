package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.AddressDTO;
import com.rotacerta.application.usecase.AddressUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.exception.EntityNotFoundException;
import com.rotacerta.domain.model.Address;
import com.rotacerta.domain.repository.AddressRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressUseCaseImpl implements AddressUseCase {

    private final AddressRepository addressRepository;

    public AddressUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO create(AddressDTO dto) {
        Address address = mapToDomain(dto);
        address.setCompanyId(TenantContext.getTenantId());
        address = addressRepository.save(address);
        return mapToDTO(address);
    }

    @Override
    public AddressDTO update(UUID id, AddressDTO dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        if (!address.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setComplement(dto.getComplement());
        address.setNeighborhood(dto.getNeighborhood());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());

        address = addressRepository.save(address);
        return mapToDTO(address);
    }

    @Override
    public AddressDTO findById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        if (!address.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        return mapToDTO(address);
    }

    @Override
    public List<AddressDTO> findAll() {
        return addressRepository.findAllByCompanyId(TenantContext.getTenantId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        if (!address.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        addressRepository.deleteById(id);
    }

    private Address mapToDomain(AddressDTO dto) {
        return Address.builder()
                .id(dto.getId())
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .neighborhood(dto.getNeighborhood())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private AddressDTO mapToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setNeighborhood(address.getNeighborhood());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        return dto;
    }
}
