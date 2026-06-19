package com.rotacerta.application.dto;

import java.math.BigDecimal;

public class DashboardDTO {
    private Double totalKmSaved;
    private Long totalTimeSaved;
    private BigDecimal totalCostSaved;
    private Integer totalDeliveries;
    private Double avgCapacityUtilization;

    public DashboardDTO() {}

    public DashboardDTO(Double totalKmSaved, Long totalTimeSaved, BigDecimal totalCostSaved, Integer totalDeliveries, Double avgCapacityUtilization) {
        this.totalKmSaved = totalKmSaved;
        this.totalTimeSaved = totalTimeSaved;
        this.totalCostSaved = totalCostSaved;
        this.totalDeliveries = totalDeliveries;
        this.avgCapacityUtilization = avgCapacityUtilization;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Double totalKmSaved;
        private Long totalTimeSaved;
        private BigDecimal totalCostSaved;
        private Integer totalDeliveries;
        private Double avgCapacityUtilization;

        public Builder totalKmSaved(Double totalKmSaved) { this.totalKmSaved = totalKmSaved; return this; }
        public Builder totalTimeSaved(Long totalTimeSaved) { this.totalTimeSaved = totalTimeSaved; return this; }
        public Builder totalCostSaved(BigDecimal totalCostSaved) { this.totalCostSaved = totalCostSaved; return this; }
        public Builder totalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; return this; }
        public Builder avgCapacityUtilization(Double avgCapacityUtilization) { this.avgCapacityUtilization = avgCapacityUtilization; return this; }

        public DashboardDTO build() {
            return new DashboardDTO(totalKmSaved, totalTimeSaved, totalCostSaved, totalDeliveries, avgCapacityUtilization);
        }
    }

    public Double getTotalKmSaved() { return totalKmSaved; }
    public void setTotalKmSaved(Double totalKmSaved) { this.totalKmSaved = totalKmSaved; }
    public Long getTotalTimeSaved() { return totalTimeSaved; }
    public void setTotalTimeSaved(Long totalTimeSaved) { this.totalTimeSaved = totalTimeSaved; }
    public BigDecimal getTotalCostSaved() { return totalCostSaved; }
    public void setTotalCostSaved(BigDecimal totalCostSaved) { this.totalCostSaved = totalCostSaved; }
    public Integer getTotalDeliveries() { return totalDeliveries; }
    public void setTotalDeliveries(Integer totalDeliveries) { this.totalDeliveries = totalDeliveries; }
    public Double getAvgCapacityUtilization() { return avgCapacityUtilization; }
    public void setAvgCapacityUtilization(Double avgCapacityUtilization) { this.avgCapacityUtilization = avgCapacityUtilization; }
}
