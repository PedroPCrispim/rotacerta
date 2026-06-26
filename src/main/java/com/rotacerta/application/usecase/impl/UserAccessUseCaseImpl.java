package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.DashboardViewAccessDTO;
import com.rotacerta.application.dto.UserAccessDTO;
import com.rotacerta.application.dto.UserAccessUpdateDTO;
import com.rotacerta.application.usecase.UserAccessUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.model.User;
import com.rotacerta.domain.repository.UserRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserAccessUseCaseImpl implements UserAccessUseCase {

    private final UserRepository userRepository;

    public UserAccessUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DashboardViewAccessDTO getCurrentUserAccess() {
        return mapAccess(getCurrentUser());
    }

    @Override
    public List<UserAccessDTO> findAllCompanyUsers() {
        User currentUser = getCurrentUser();
        validateAdmin(currentUser);

        UUID tenantId = TenantContext.getTenantId();
        return userRepository.findAllByCompanyId(tenantId).stream()
                .map(this::mapUserAccess)
                .toList();
    }

    @Override
    @Transactional
    public UserAccessDTO updateUserAccess(UserAccessUpdateDTO dto) {
        User currentUser = getCurrentUser();
        validateAdmin(currentUser);
        validateAtLeastOneView(dto.getViewAccess());

        User targetUser = userRepository.findByEmail(dto.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new BusinessException("Usuario informado nao foi encontrado"));

        if (!currentUser.getCompanyId().equals(targetUser.getCompanyId())) {
            throw new BusinessException("Acesso negado");
        }

        if (targetUser.getRole() == User.UserRole.ADMIN) {
            throw new BusinessException("O administrador sempre possui acesso total");
        }

        targetUser.setCanViewOperational(dto.getViewAccess().isOperational());
        targetUser.setCanViewFinancial(dto.getViewAccess().isFinancial());
        targetUser.setCanViewFleet(dto.getViewAccess().isFleet());

        return mapUserAccess(userRepository.save(targetUser));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Usuario autenticado nao encontrado");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BusinessException("Usuario autenticado nao encontrado"));
    }

    private void validateAdmin(User user) {
        if (user.getRole() != User.UserRole.ADMIN) {
            throw new BusinessException("Acesso negado");
        }
    }

    private void validateAtLeastOneView(DashboardViewAccessDTO access) {
        if (!access.isOperational() && !access.isFinancial() && !access.isFleet()) {
            throw new BusinessException("Pelo menos uma visao deve permanecer liberada");
        }
    }

    private UserAccessDTO mapUserAccess(User user) {
        return UserAccessDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .viewAccess(mapAccess(user))
                .build();
    }

    private DashboardViewAccessDTO mapAccess(User user) {
        if (user.getRole() == User.UserRole.ADMIN) {
            return DashboardViewAccessDTO.builder()
                    .operational(true)
                    .financial(true)
                    .fleet(true)
                    .build();
        }

        return DashboardViewAccessDTO.builder()
                .operational(user.isCanViewOperational())
                .financial(user.isCanViewFinancial())
                .fleet(user.isCanViewFleet())
                .build();
    }
}
