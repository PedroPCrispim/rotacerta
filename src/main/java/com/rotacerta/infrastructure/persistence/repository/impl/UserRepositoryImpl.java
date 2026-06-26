package com.rotacerta.infrastructure.persistence.repository.impl;

import com.rotacerta.domain.model.User;
import com.rotacerta.domain.repository.UserRepository;
import com.rotacerta.infrastructure.persistence.entity.UserEntity;
import com.rotacerta.infrastructure.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = user.getId() == null
                ? new UserEntity()
                : jpaUserRepository.findById(user.getId()).orElseGet(UserEntity::new);

        entity.setCompanyId(user.getCompanyId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole().name());
        entity.setCanViewOperational(user.isCanViewOperational());
        entity.setCanViewFinancial(user.isCanViewFinancial());
        entity.setCanViewFleet(user.isCanViewFleet());

        entity = jpaUserRepository.save(entity);
        return mapToDomain(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::mapToDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<User> findAllByCompanyId(UUID companyId) {
        return jpaUserRepository.findAllByCompanyIdOrderByNameAsc(companyId).stream()
                .map(this::mapToDomain)
                .toList();
    }

    private User mapToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .companyId(entity.getCompanyId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(User.UserRole.valueOf(entity.getRole()))
                .canViewOperational(entity.isCanViewOperational())
                .canViewFinancial(entity.isCanViewFinancial())
                .canViewFleet(entity.isCanViewFleet())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
