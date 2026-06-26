package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.DashboardViewAccessDTO;
import com.rotacerta.application.dto.UserAccessDTO;
import com.rotacerta.application.dto.UserAccessUpdateDTO;

import java.util.List;

public interface UserAccessUseCase {
    DashboardViewAccessDTO getCurrentUserAccess();
    List<UserAccessDTO> findAllCompanyUsers();
    UserAccessDTO updateUserAccess(UserAccessUpdateDTO dto);
}
