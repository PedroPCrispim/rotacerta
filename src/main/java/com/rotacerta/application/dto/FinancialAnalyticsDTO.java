package com.rotacerta.application.dto;

import java.math.BigDecimal;
import java.util.Map;

public class FinancialAnalyticsDTO {
    private BigDecimal totalEstimatedCost;
    private BigDecimal totalCostSaved;
    private Map<String, BigDecimal> savingsByMonth;

    public FinancialAnalyticsDTO() {}

    public FinancialAnalyticsDTO(BigDecimal totalEstimatedCost, BigDecimal totalCostSaved, Map<String, BigDecimal> savingsByMonth) {
        this.totalEstimatedCost = totalEstimatedCost;
        this.totalCostSaved = totalCostSaved;
        this.savingsByMonth = savingsByMonth;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal totalEstimatedCost;
        private BigDecimal totalCostSaved;
        private Map<String, BigDecimal> savingsByMonth;

        public Builder totalEstimatedCost(BigDecimal totalEstimatedCost) { this.totalEstimatedCost = totalEstimatedCost; return this; }
        public Builder totalCostSaved(BigDecimal totalCostSaved) { this.totalCostSaved = totalCostSaved; return this; }
        public Builder savingsByMonth(Map<String, BigDecimal> savingsByMonth) { this.savingsByMonth = savingsByMonth; return this; }

        public FinancialAnalyticsDTO build() {
            return new FinancialAnalyticsDTO(totalEstimatedCost, totalCostSaved, savingsByMonth);
        }
    }

    public BigDecimal getTotalEstimatedCost() { return totalEstimatedCost; }
    public void setTotalEstimatedCost(BigDecimal totalEstimatedCost) { this.totalEstimatedCost = totalEstimatedCost; }
    public BigDecimal getTotalCostSaved() { return totalCostSaved; }
    public void setTotalCostSaved(BigDecimal totalCostSaved) { this.totalCostSaved = totalCostSaved; }
    public Map<String, BigDecimal> getSavingsByMonth() { return savingsByMonth; }
    public void setSavingsByMonth(Map<String, BigDecimal> savingsByMonth) { this.savingsByMonth = savingsByMonth; }
}
