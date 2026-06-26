package com.rotacerta.infrastructure.web.controller;

import com.rotacerta.application.dto.DashboardViewAccessDTO;
import com.rotacerta.application.dto.UserAccessDTO;
import com.rotacerta.application.dto.UserAccessUpdateDTO;
import com.rotacerta.application.usecase.UserAccessUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/access")
@Tag(name = "Usuarios", description = "Permissoes de visualizacao da dashboard por usuario")
public class UserAccessController {

    private final UserAccessUseCase userAccessUseCase;

    public UserAccessController(UserAccessUseCase userAccessUseCase) {
        this.userAccessUseCase = userAccessUseCase;
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna as permissoes de visualizacao do usuario autenticado")
    public ResponseEntity<DashboardViewAccessDTO> getCurrentUserAccess() {
        return ResponseEntity.ok(userAccessUseCase.getCurrentUserAccess());
    }

    @GetMapping
    @Operation(summary = "Lista os usuarios da empresa com suas permissoes de visualizacao")
    public ResponseEntity<List<UserAccessDTO>> findAllCompanyUsers() {
        return ResponseEntity.ok(userAccessUseCase.findAllCompanyUsers());
    }

    @PutMapping
    @Operation(summary = "Atualiza as permissoes de visualizacao de um usuario da empresa")
    public ResponseEntity<UserAccessDTO> updateUserAccess(@RequestBody @Valid UserAccessUpdateDTO dto) {
        return ResponseEntity.ok(userAccessUseCase.updateUserAccess(dto));
    }
}
