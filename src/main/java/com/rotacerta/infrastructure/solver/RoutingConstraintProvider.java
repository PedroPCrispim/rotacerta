package com.rotacerta.infrastructure.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import com.rotacerta.infrastructure.solver.model.PlanningVehicle;
import com.rotacerta.infrastructure.solver.model.PlanningVisit;

public class RoutingConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                vehicleCapacity(factory),
                minimizeDistance(factory)
        };
    }

    protected Constraint vehicleCapacity(ConstraintFactory factory) {
        return factory.forEach(PlanningVehicle.class)
                .filter(vehicle -> {
                    double totalDemand = vehicle.getVisitList().stream()
                            .mapToDouble(PlanningVisit::getDemand)
                            .sum();
                    return totalDemand > vehicle.getCapacity();
                })
                .penalizeLong(HardSoftLongScore.ONE_HARD, vehicle -> {
                    double totalDemand = vehicle.getVisitList().stream()
                            .mapToDouble(PlanningVisit::getDemand)
                            .sum();
                    return (long) (totalDemand - vehicle.getCapacity());
                })
                .asConstraint("Capacidade do Veículo");
    }

    protected Constraint minimizeDistance(ConstraintFactory factory) {
        return factory.forEach(PlanningVehicle.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT, PlanningVehicle::getTotalDistance)
                .asConstraint("Minimizar Distância");
    }
}
