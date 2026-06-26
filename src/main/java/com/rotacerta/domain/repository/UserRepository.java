package com.rotacerta.domain.repository;

import com.rotacerta.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findAllByCompanyId(UUID companyId);
}
