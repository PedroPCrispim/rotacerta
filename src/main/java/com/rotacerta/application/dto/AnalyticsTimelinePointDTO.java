package com.rotacerta.application.dto;

public class AnalyticsTimelinePointDTO {
    private String label;
    private String comparisonLabel;
    private Double currentValue;
    private Double previousValue;

    public AnalyticsTimelinePointDTO() {}

    public AnalyticsTimelinePointDTO(String label, String comparisonLabel, Double currentValue, Double previousValue) {
        this.label = label;
        this.comparisonLabel = comparisonLabel;
        this.currentValue = currentValue;
        this.previousValue = previousValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String label;
        private String comparisonLabel;
        private Double currentValue;
        private Double previousValue;

        public Builder label(String label) { this.label = label; return this; }
        public Builder comparisonLabel(String comparisonLabel) { this.comparisonLabel = comparisonLabel; return this; }
        public Builder currentValue(Double currentValue) { this.currentValue = currentValue; return this; }
        public Builder previousValue(Double previousValue) { this.previousValue = previousValue; return this; }

        public AnalyticsTimelinePointDTO build() {
            return new AnalyticsTimelinePointDTO(label, comparisonLabel, currentValue, previousValue);
        }
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getComparisonLabel() { return comparisonLabel; }
    public void setComparisonLabel(String comparisonLabel) { this.comparisonLabel = comparisonLabel; }
    public Double getCurrentValue() { return currentValue; }
    public void setCurrentValue(Double currentValue) { this.currentValue = currentValue; }
    public Double getPreviousValue() { return previousValue; }
    public void setPreviousValue(Double previousValue) { this.previousValue = previousValue; }
}
