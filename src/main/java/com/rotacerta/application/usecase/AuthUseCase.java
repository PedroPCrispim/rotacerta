package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.AuthResponseDTO;
import com.rotacerta.application.dto.LoginRequestDTO;

public interface AuthUseCase {
    AuthResponseDTO login(LoginRequestDTO request);
}
