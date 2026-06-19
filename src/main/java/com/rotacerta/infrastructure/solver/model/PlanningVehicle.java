package com.rotacerta.infrastructure.solver.model;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@PlanningEntity
public class PlanningVehicle {
    private UUID id;
    private Location depotLocation;
    private double capacity;

    @PlanningListVariable(valueRangeProviderRefs = "visitRange")
    private List<PlanningVisit> visitList = new ArrayList<>();

    public PlanningVehicle() {}

    public PlanningVehicle(UUID id, Location depotLocation, double capacity, List<PlanningVisit> visitList) {
        this.id = id;
        this.depotLocation = depotLocation;
        this.capacity = capacity;
        this.visitList = visitList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private Location depotLocation;
        private double capacity;
        private List<PlanningVisit> visitList;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder depotLocation(Location depotLocation) { this.depotLocation = depotLocation; return this; }
        public Builder capacity(double capacity) { this.capacity = capacity; return this; }
        public Builder visitList(List<PlanningVisit> visitList) { this.visitList = visitList; return this; }

        public PlanningVehicle build() {
            return new PlanningVehicle(id, depotLocation, capacity, visitList);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Location getDepotLocation() { return depotLocation; }
    public void setDepotLocation(Location depotLocation) { this.depotLocation = depotLocation; }
    public double getCapacity() { return capacity; }
    public void setCapacity(double capacity) { this.capacity = capacity; }
    public List<PlanningVisit> getVisitList() { return visitList; }
    public void setVisitList(List<PlanningVisit> visitList) { this.visitList = visitList; }

    public long getTotalDistance() {
        if (visitList == null || visitList.isEmpty()) return 0;
        long total = 0;
        Location current = depotLocation;
        for (PlanningVisit visit : visitList) {
            total += current.getDistanceTo(visit.getLocation());
            current = visit.getLocation();
        }
        total += current.getDistanceTo(depotLocation); // volta para a base
        return total;
    }
}
