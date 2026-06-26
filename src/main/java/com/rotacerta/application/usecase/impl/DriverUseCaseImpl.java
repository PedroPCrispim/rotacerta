package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.DriverDTO;
import com.rotacerta.application.dto.DriverPortalDTO;
import com.rotacerta.application.usecase.DriverUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.exception.EntityNotFoundException;
import com.rotacerta.domain.model.Address;
import com.rotacerta.domain.model.Driver;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.AddressRepository;
import com.rotacerta.domain.repository.DriverRepository;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverUseCaseImpl implements DriverUseCase {
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final AddressRepository addressRepository;

    public DriverUseCaseImpl(DriverRepository driverRepository, VehicleRepository vehicleRepository, AddressRepository addressRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public DriverDTO create(DriverDTO dto) {
        Driver driver = mapToDomain(dto);
        driver.setCompanyId(TenantContext.getTenantId());
        validateVehicleAssignment(driver.getAssignedVehicleId(), driver.getCompanyId());
        validateNextStopAssignment(driver.getNextStopAddressId(), driver.getCompanyId());
        return mapToDTO(driverRepository.save(driver));
    }

    @Override
    public DriverDTO update(UUID id, DriverDTO dto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        validateOwnership(driver.getCompanyId());

        driver.setName(dto.getName());
        driver.setEmail(dto.getEmail());
        driver.setPhone(dto.getPhone());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setStatus(dto.getStatus());
        driver.setAssignedVehicleId(dto.getAssignedVehicleId());
        driver.setNextStopAddressId(dto.getNextStopAddressId());
        driver.setCurrentRouteLabel(dto.getCurrentRouteLabel());
        driver.setRouteExecutionStatus(dto.getRouteExecutionStatus());
        driver.setCheckInRequired(dto.getCheckInRequired());
        validateVehicleAssignment(driver.getAssignedVehicleId(), driver.getCompanyId());
        validateNextStopAssignment(driver.getNextStopAddressId(), driver.getCompanyId());

        return mapToDTO(driverRepository.save(driver));
    }

    @Override
    public DriverDTO findById(UUID id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        validateOwnership(driver.getCompanyId());
        return mapToDTO(driver);
    }

    @Override
    public List<DriverDTO> findAll() {
        return driverRepository.findAllByCompanyId(TenantContext.getTenantId()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverPortalDTO getPortalById(UUID id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        validateOwnership(driver.getCompanyId());

        DriverPortalDTO dto = new DriverPortalDTO();
        dto.setDriverId(driver.getId());
        dto.setDriverName(driver.getName());
        dto.setStatus(driver.getStatus());
        dto.setPhone(driver.getPhone());
        dto.setAssignedVehicleId(driver.getAssignedVehicleId());
        dto.setAssignedVehicleLabel(resolveVehicleLabel(driver.getAssignedVehicleId()));
        dto.setNextStopAddressId(driver.getNextStopAddressId());
        Address nextStop = resolveNextStopAddress(driver.getNextStopAddressId());
        dto.setNextStopLabel(formatAddressLabel(nextStop));
        dto.setNextStopFullAddress(formatFullAddress(nextStop));
        dto.setNextStopLatitude(nextStop == null ? null : nextStop.getLatitude());
        dto.setNextStopLongitude(nextStop == null ? null : nextStop.getLongitude());
        dto.setCurrentRouteLabel(driver.getCurrentRouteLabel());
        dto.setRouteExecutionStatus(driver.getRouteExecutionStatus());
        dto.setCheckInRequired(Boolean.TRUE.equals(driver.getCheckInRequired()));
        return dto;
    }

    @Override
    public void delete(UUID id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado"));
        validateOwnership(driver.getCompanyId());
        driverRepository.deleteById(id);
    }

    private Driver mapToDomain(DriverDTO dto) {
        return Driver.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .licenseNumber(dto.getLicenseNumber())
                .status(dto.getStatus())
                .assignedVehicleId(dto.getAssignedVehicleId())
                .nextStopAddressId(dto.getNextStopAddressId())
                .currentRouteLabel(dto.getCurrentRouteLabel())
                .routeExecutionStatus(dto.getRouteExecutionStatus())
                .checkInRequired(Boolean.TRUE.equals(dto.getCheckInRequired()))
                .build();
    }

    private DriverDTO mapToDTO(Driver driver) {
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setEmail(driver.getEmail());
        dto.setPhone(driver.getPhone());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setStatus(driver.getStatus());
        dto.setAssignedVehicleId(driver.getAssignedVehicleId());
        dto.setAssignedVehicleLabel(resolveVehicleLabel(driver.getAssignedVehicleId()));
        dto.setNextStopAddressId(driver.getNextStopAddressId());
        dto.setNextStopLabel(formatAddressLabel(resolveNextStopAddress(driver.getNextStopAddressId())));
        dto.setCurrentRouteLabel(driver.getCurrentRouteLabel());
        dto.setRouteExecutionStatus(driver.getRouteExecutionStatus());
        dto.setCheckInRequired(Boolean.TRUE.equals(driver.getCheckInRequired()));
        return dto;
    }

    private void validateVehicleAssignment(UUID vehicleId, UUID companyId) {
        if (vehicleId == null) {
            return;
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new BusinessException("Veículo vinculado não encontrado"));

        if (!companyId.equals(vehicle.getCompanyId())) {
            throw new BusinessException("O veículo informado não pertence à empresa");
        }
    }

    private void validateNextStopAssignment(UUID addressId, UUID companyId) {
        if (addressId == null) {
            return;
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException("Endereco da proxima parada nao encontrado"));

        if (!companyId.equals(address.getCompanyId())) {
            throw new BusinessException("O endereco informado nao pertence a empresa");
        }
    }

    private void validateOwnership(UUID companyId) {
        if (!TenantContext.getTenantId().equals(companyId)) {
            throw new BusinessException("Acesso negado");
        }
    }

    private String resolveVehicleLabel(UUID vehicleId) {
        if (vehicleId == null) {
            return "Sem veículo";
        }

        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> vehicle.getModel() + " • " + vehicle.getPlate())
                .orElse("Veículo não encontrado");
    }

    private Address resolveNextStopAddress(UUID addressId) {
        if (addressId == null) {
            return null;
        }

        return addressRepository.findById(addressId)
                .filter(address -> TenantContext.getTenantId().equals(address.getCompanyId()))
                .orElse(null);
    }

    private String formatAddressLabel(Address address) {
        if (address == null) {
            return "Sem parada definida";
        }

        return address.getStreet() + ", " + address.getNumber() + " • " + address.getCity();
    }

    private String formatFullAddress(Address address) {
        if (address == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(address.getStreet()).append(", ").append(address.getNumber());

        if (address.getComplement() != null && !address.getComplement().isBlank()) {
            builder.append(" - ").append(address.getComplement());
        }

        builder.append(" - ")
                .append(address.getNeighborhood())
                .append(" - ")
                .append(address.getCity())
                .append("/")
                .append(address.getState())
                .append(" - CEP ")
                .append(address.getZipCode());

        return builder.toString();
    }
}
