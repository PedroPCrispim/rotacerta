package com.rotacerta.application.service;

import com.rotacerta.domain.model.User;

public interface TokenService {
    String generateToken(User user);
    String validateToken(String token);
}
