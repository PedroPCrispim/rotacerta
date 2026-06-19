package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.Company;
import com.rotacerta.domain.repository.CompanyRepository;
import com.rotacerta.infrastructure.persistence.entity.CompanyEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaCompanyRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CompanyRepositoryImpl implements CompanyRepository {

    private final JpaCompanyRepository jpaCompanyRepository;

    public CompanyRepositoryImpl(JpaCompanyRepository jpaCompanyRepository) {
        this.jpaCompanyRepository = jpaCompanyRepository;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = CompanyEntity.builder()
                .id(company.getId())
                .name(company.getName())
                .cnpj(company.getCnpj())
                .active(company.isActive())
                .build();
        entity = jpaCompanyRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpaCompanyRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<Company> findByCnpj(String cnpj) {
        return jpaCompanyRepository.findByCnpj(cnpj).map(this::mapToDomain);
    }

    private Company mapToDomain(CompanyEntity entity) {
        return Company.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cnpj(entity.getCnpj())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
