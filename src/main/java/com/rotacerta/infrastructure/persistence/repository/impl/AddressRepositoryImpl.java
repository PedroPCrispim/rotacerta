package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.Address;
import com.rotacerta.domain.repository.AddressRepository;
import com.rotacerta.infrastructure.persistence.entity.AddressEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaAddressRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AddressRepositoryImpl implements AddressRepository {

    private final JpaAddressRepository jpaAddressRepository;

    public AddressRepositoryImpl(JpaAddressRepository jpaAddressRepository) {
        this.jpaAddressRepository = jpaAddressRepository;
    }

    @Override
    public Address save(Address address) {
        AddressEntity entity = AddressEntity.builder()
                .id(address.getId())
                .companyId(address.getCompanyId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
        entity = jpaAddressRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<Address> findById(UUID id) {
        return jpaAddressRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Address> findAllByCompanyId(UUID companyId) {
        return jpaAddressRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaAddressRepository.deleteById(id);
    }

    private Address mapToDomain(AddressEntity entity) {
        return Address.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .neighborhood(entity.getNeighborhood())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
