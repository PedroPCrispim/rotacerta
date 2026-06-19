package com.rotacerta.application.usecase.impl;

import ai.timefold.solver.core.api.solver.SolverManager;
import com.rotacerta.application.dto.DeliveryPointDTO;
import com.rotacerta.application.dto.RoutingRequestDTO;
import com.rotacerta.application.dto.RoutingResultDTO;
import com.rotacerta.application.dto.VehicleRouteDTO;
import com.rotacerta.application.service.MapLinkService;
import com.rotacerta.application.usecase.RoutingUseCase;
import com.rotacerta.domain.exception.BusinessException;
import com.rotacerta.domain.model.Address;
import com.rotacerta.domain.model.DeliveryPoint;
import com.rotacerta.domain.model.Vehicle;
import com.rotacerta.domain.repository.AddressRepository;
import com.rotacerta.domain.repository.DeliveryPointRepository;
import com.rotacerta.domain.repository.VehicleRepository;
import com.rotacerta.infrastructure.multitenancy.TenantContext;
import com.rotacerta.infrastructure.solver.model.Location;
import com.rotacerta.infrastructure.solver.model.PlanningVehicle;
import com.rotacerta.infrastructure.solver.model.PlanningVisit;
import com.rotacerta.infrastructure.solver.model.RoutingSolution;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoutingUseCaseImpl implements RoutingUseCase {

    private final VehicleRepository vehicleRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final AddressRepository addressRepository;
    private final SolverManager<RoutingSolution, UUID> solverManager;
    private final MapLinkService mapLinkService;

    public RoutingUseCaseImpl(VehicleRepository vehicleRepository, DeliveryPointRepository deliveryPointRepository,
                               AddressRepository addressRepository, SolverManager<RoutingSolution, UUID> solverManager,
                               MapLinkService mapLinkService) {
        this.vehicleRepository = vehicleRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.addressRepository = addressRepository;
        this.solverManager = solverManager;
        this.mapLinkService = mapLinkService;
    }

    @Override
    public RoutingResultDTO calculateRoute(RoutingRequestDTO request) {
        UUID companyId = TenantContext.getTenantId();

        Address depotAddress = addressRepository.findById(request.getDepotAddressId())
                .orElseThrow(() -> new BusinessException("Endereço da base não encontrado"));

        List<Vehicle> vehicles = vehicleRepository.findAllByCompanyId(companyId);
        List<DeliveryPoint> points = deliveryPointRepository.findAllByCompanyId(companyId);

        if (vehicles.isEmpty() || points.isEmpty()) {
            throw new BusinessException("É necessário ter pelo menos um veículo e um ponto de entrega.");
        }

        // Preparar a solução para o solver
        RoutingSolution problem = prepareProblem(depotAddress, vehicles, points);

        // Resolver (Síncrono para o MVP, mas SolverManager suporta Assíncrono)
        UUID problemId = UUID.randomUUID();
        RoutingSolution solution;
        try {
            solution = solverManager.solve(problemId, problem).getFinalBestSolution();
        } catch (Exception e) {
            throw new BusinessException("Erro ao calcular a rota: " + e.getMessage());
        }

        return mapToResult(solution);
    }

    private RoutingSolution prepareProblem(Address depot, List<Vehicle> vehicles, List<DeliveryPoint> points) {
        Location depotLoc = new Location(depot.getLatitude(), depot.getLongitude());

        List<PlanningVehicle> planningVehicles = vehicles.stream()
                .map(v -> PlanningVehicle.builder()
                        .id(v.getId())
                        .depotLocation(depotLoc)
                        .capacity(v.getCapacity())
                        .visitList(new ArrayList<>())
                        .build())
                .collect(Collectors.toList());

        List<PlanningVisit> planningVisits = points.stream()
                .map(p -> {
                    // Buscar o endereço do ponto
                    Address addr = addressRepository.findById(p.getAddressId()).get();
                    return PlanningVisit.builder()
                            .id(p.getId())
                            .location(new Location(addr.getLatitude(), addr.getLongitude()))
                            .demand(1.0) // MVP: cada entrega conta como 1 unidade
                            .priority(p.getPriority())
                            .build();
                })
                .collect(Collectors.toList());

        return new RoutingSolution(planningVehicles, planningVisits, null);
    }

    private RoutingResultDTO mapToResult(RoutingSolution solution) {
        List<VehicleRouteDTO> routes = solution.getVehicles().stream()
                .map(pv -> {
                    List<DeliveryPointDTO> visitsDTO = pv.getVisitList().stream()
                            .map(visit -> {
                                DeliveryPointDTO dto = new DeliveryPointDTO();
                                dto.setId(visit.getId());
                                dto.setDescription("Ponto " + visit.getId());
                                return dto;
                            })
                            .collect(Collectors.toList());

                    List<Location> locations = pv.getVisitList().stream()
                            .map(PlanningVisit::getLocation)
                            .collect(Collectors.toList());

                    String googleMapsUrl = mapLinkService.generateGoogleMapsUrl(pv.getDepotLocation(), locations);
                    String wazeUrl = locations.isEmpty() ? null : mapLinkService.generateWazeUrl(locations.get(0));

                    return VehicleRouteDTO.builder()
                            .vehicleId(pv.getId())
                            .visits(visitsDTO)
                            .distance(pv.getTotalDistance())
                            .googleMapsUrl(googleMapsUrl)
                            .wazeUrl(wazeUrl)
                            .build();
                })
                .collect(Collectors.toList());

        long totalDistance = routes.stream().mapToLong(VehicleRouteDTO::getDistance).sum();

        return RoutingResultDTO.builder()
                .routes(routes)
                .totalDistance(totalDistance)
                .totalEstimatedTime(totalDistance / 10) // Estimativa simples: 10m/s
                .build();
    }
}
