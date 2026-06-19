package com.rotacerta.application.usecase.impl;

import com.rotacerta.application.dto.AuthResponseDTO;
import com.rotacerta.application.dto.LoginRequestDTO;
import com.rotacerta.application.service.TokenService;
import com.rotacerta.application.usecase.AuthUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.model.User;
import com.rotacerta.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCaseImpl implements AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = tokenService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .companyId(user.getCompanyId())
                .build();
    }
}
