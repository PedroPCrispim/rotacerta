package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.Company;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(UUID id);
    Optional<Company> findByCnpj(String cnpj);
}
