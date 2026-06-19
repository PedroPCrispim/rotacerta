package com.rotacerta.application.usecase;

import com.rotacerta.application.dto.RoutingRequestDTO;
import com.rotacerta.application.dto.RoutingResultDTO;

public interface RoutingUseCase {
    RoutingResultDTO calculateRoute(RoutingRequestDTO request);
}
