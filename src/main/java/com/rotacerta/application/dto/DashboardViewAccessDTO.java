package com.rotacerta.application.dto;

public class DashboardViewAccessDTO {
    private boolean operational;
    private boolean financial;
    private boolean fleet;

    public DashboardViewAccessDTO() {}

    public DashboardViewAccessDTO(boolean operational, boolean financial, boolean fleet) {
        this.operational = operational;
        this.financial = financial;
        this.fleet = fleet;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean operational;
        private boolean financial;
        private boolean fleet;

        public Builder operational(boolean operational) { this.operational = operational; return this; }
        public Builder financial(boolean financial) { this.financial = financial; return this; }
        public Builder fleet(boolean fleet) { this.fleet = fleet; return this; }

        public DashboardViewAccessDTO build() {
            return new DashboardViewAccessDTO(operational, financial, fleet);
        }
    }

    public boolean isOperational() { return operational; }
    public void setOperational(boolean operational) { this.operational = operational; }
    public boolean isFinancial() { return financial; }
    public void setFinancial(boolean financial) { this.financial = financial; }
    public boolean isFleet() { return fleet; }
    public void setFleet(boolean fleet) { this.fleet = fleet; }
}
