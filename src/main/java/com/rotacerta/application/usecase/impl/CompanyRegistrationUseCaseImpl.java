package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.CompanyRegistrationDTO;
import com.rotacerta.application.usecase.CompanyRegistrationUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.model.Company;
import com.rotacerta.domain.model.User;
import com.rotacerta.domain.repository.CompanyRepository;
import com.rotacerta.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CompanyRegistrationUseCaseImpl implements CompanyRegistrationUseCase {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyRegistrationUseCaseImpl(CompanyRepository companyRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(CompanyRegistrationDTO request) {
        if (companyRepository.findByCnpj(request.getCnpj()).isPresent()) {
            throw new BusinessException("CNPJ já cadastrado");
        }

        if (userRepository.findByEmail(request.getAdminEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        Company company = Company.builder()
                .name(request.getCompanyName())
                .cnpj(request.getCnpj())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        company = companyRepository.save(company);

        User admin = User.builder()
                .companyId(company.getId())
                .name(request.getAdminName())
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .role(User.UserRole.ADMIN)
                .canViewOperational(true)
                .canViewFinancial(true)
                .canViewFleet(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
    }
}
