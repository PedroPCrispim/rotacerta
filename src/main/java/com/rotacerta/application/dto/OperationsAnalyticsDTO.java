package com.rotacerta.application.dto;

import java.util.Map;

public class OperationsAnalyticsDTO {
    private Integer totalRoutes;
    private Integer totalDeliveries;
    private Map<String, Integer> deliveriesByDay;

    public OperationsAnalyticsDTO() {}

    public OperationsAnalyticsDTO(Integer totalRoutes, Integer totalDeliveries, Map<String, Integer> deliveriesByDay) {
        this.totalRoutes = totalRoutes;
        this.totalDeliveries = totalDeliveries;
        this.deliveriesByDay = deliveriesByDay;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer totalRoutes;
        private Integer totalDeliveries;
        private Map<String, Integer> deliveriesByDay;

        public Builder totalRoutes(Integer totalRoutes) { this.totalRoutes = totalRoutes; return this; }
        public Builder totalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; return this; }
        public Builder deliveriesByDay(Map<String, Integer> deliveriesByDay) { this.deliveriesByDay = deliveriesByDay; return this; }

        public OperationsAnalyticsDTO build() {
            return new OperationsAnalyticsDTO(totalRoutes, totalDeliveries, deliveriesByDay);
        }
    }

    public Integer getTotalRoutes() { return totalRoutes; }
    public void setTotalRoutes(Integer totalRoutes) { this.totalRoutes = totalRoutes; }
    public Integer getTotalDeliveries() { return totalDeliveries; }
    public void setTotalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; }
    public Map<String, Integer> getDeliveriesByDay() { return deliveriesByDay; }
    public void setDeliveriesByDay(Map<String, Integer> deliveriesByDay) { this.deliveriesByDay = deliveriesByDay; }
}
