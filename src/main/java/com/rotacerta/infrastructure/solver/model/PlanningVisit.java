package com.rotacerta.infrastructure.solver.model;

import java.util.UUID;

public class PlanningVisit {
    private UUID id;
    private Location location;
    private int priority; // 1 high, 5 low
    private double demand;

    public PlanningVisit() {}

    public PlanningVisit(UUID id, Location location, int priority, double demand) {
        this.id = id;
        this.location = location;
        this.priority = priority;
        this.demand = demand;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private Location location;
        private int priority;
        private double demand;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder location(Location location) { this.location = location; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder demand(double demand) { this.demand = demand; return this; }

        public PlanningVisit build() {
            return new PlanningVisit(id, location, priority, demand);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public double getDemand() { return demand; }
    public void setDemand(double demand) { this.demand = demand; }
}
