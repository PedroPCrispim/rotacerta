package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.Address;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(UUID id);
    List<Address> findAllByCompanyId(UUID companyId);
    void deleteById(UUID id);
}
