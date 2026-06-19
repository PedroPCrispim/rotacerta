package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.DeliveryPointDTO;
import com.rotacerta.application.usecase.DeliveryPointUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.exception.EntityNotFoundException;
import com.rotacerta.domain.model.DeliveryPoint;
import com.rotacerta.domain.repository.AddressRepository;
import com.rotacerta.domain.repository.DeliveryPointRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliveryPointUseCaseImpl implements DeliveryPointUseCase {

    private final DeliveryPointRepository deliveryPointRepository;
    private final AddressRepository addressRepository;

    public DeliveryPointUseCaseImpl(DeliveryPointRepository deliveryPointRepository, AddressRepository addressRepository) {
        this.deliveryPointRepository = deliveryPointRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public DeliveryPointDTO create(DeliveryPointDTO dto) {
        validateAddress(dto.getAddressId());

        DeliveryPoint deliveryPoint = mapToDomain(dto);
        deliveryPoint.setCompanyId(TenantContext.getTenantId());
        deliveryPoint = deliveryPointRepository.save(deliveryPoint);
        return mapToDTO(deliveryPoint);
    }

    @Override
    public DeliveryPointDTO update(UUID id, DeliveryPointDTO dto) {
        DeliveryPoint deliveryPoint = deliveryPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de entrega não encontrado"));

        if (!deliveryPoint.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        validateAddress(dto.getAddressId());

        deliveryPoint.setAddressId(dto.getAddressId());
        deliveryPoint.setDescription(dto.getDescription());
        deliveryPoint.setContactName(dto.getContactName());
        deliveryPoint.setPriority(dto.getPriority());
        deliveryPoint.setStartTime(dto.getStartTime());
        deliveryPoint.setEndTime(dto.getEndTime());

        deliveryPoint = deliveryPointRepository.save(deliveryPoint);
        return mapToDTO(deliveryPoint);
    }

    @Override
    public DeliveryPointDTO findById(UUID id) {
        DeliveryPoint deliveryPoint = deliveryPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de entrega não encontrado"));

        if (!deliveryPoint.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        return mapToDTO(deliveryPoint);
    }

    @Override
    public List<DeliveryPointDTO> findAll() {
        return deliveryPointRepository.findAllByCompanyId(TenantContext.getTenantId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        DeliveryPoint deliveryPoint = deliveryPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ponto de entrega não encontrado"));

        if (!deliveryPoint.getCompanyId().equals(TenantContext.getTenantId())) {
            throw new BusinessException("Acesso negado");
        }

        deliveryPointRepository.deleteById(id);
    }

    private void validateAddress(UUID addressId) {
        addressRepository.findById(addressId)
                .filter(a -> a.getCompanyId().equals(TenantContext.getTenantId()))
                .orElseThrow(() -> new BusinessException("Endereço inválido ou não pertence à empresa"));
    }

    private DeliveryPoint mapToDomain(DeliveryPointDTO dto) {
        return DeliveryPoint.builder()
                .id(dto.getId())
                .addressId(dto.getAddressId())
                .description(dto.getDescription())
                .contactName(dto.getContactName())
                .priority(dto.getPriority())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }

    private DeliveryPointDTO mapToDTO(DeliveryPoint deliveryPoint) {
        DeliveryPointDTO dto = new DeliveryPointDTO();
        dto.setId(deliveryPoint.getId());
        dto.setAddressId(deliveryPoint.getAddressId());
        dto.setDescription(deliveryPoint.getDescription());
        dto.setContactName(deliveryPoint.getContactName());
        dto.setPriority(deliveryPoint.getPriority());
        dto.setStartTime(deliveryPoint.getStartTime());
        dto.setEndTime(deliveryPoint.getEndTime());
        return dto;
    }
}
