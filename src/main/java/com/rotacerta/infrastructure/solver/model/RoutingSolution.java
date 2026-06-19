package com.rotacerta.infrastructure.solver.model;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.List;

@PlanningSolution
public class RoutingSolution {

    @PlanningEntityCollectionProperty
    private List<PlanningVehicle> vehicles;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "visitRange")
    private List<PlanningVisit> visits;

    @PlanningScore
    private HardSoftLongScore score;

    public RoutingSolution() {}

    public RoutingSolution(List<PlanningVehicle> vehicles, List<PlanningVisit> visits, HardSoftLongScore score) {
        this.vehicles = vehicles;
        this.visits = visits;
        this.score = score;
    }

    public List<PlanningVehicle> getVehicles() { return vehicles; }
    public void setVehicles(List<PlanningVehicle> vehicles) { this.vehicles = vehicles; }
    public List<PlanningVisit> getVisits() { return visits; }
    public void setVisits(List<PlanningVisit> visits) { this.visits = visits; }
    public HardSoftLongScore getScore() { return score; }
    public void setScore(HardSoftLongScore score) { this.score = score; }
}
