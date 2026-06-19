package com.rotacerta.application.dto;

import java.util.List;

public class AnalyticsTimelineDTO {
    private String metric;
    private String granularity;
    private Double currentTotal;
    private Double previousTotal;
    private Double variationPercentage;
    private List<AnalyticsTimelinePointDTO> points;

    public AnalyticsTimelineDTO() {}

    public AnalyticsTimelineDTO(String metric, String granularity, Double currentTotal, Double previousTotal,
                                Double variationPercentage, List<AnalyticsTimelinePointDTO> points) {
        this.metric = metric;
        this.granularity = granularity;
        this.currentTotal = currentTotal;
        this.previousTotal = previousTotal;
        this.variationPercentage = variationPercentage;
        this.points = points;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String metric;
        private String granularity;
        private Double currentTotal;
        private Double previousTotal;
        private Double variationPercentage;
        private List<AnalyticsTimelinePointDTO> points;

        public Builder metric(String metric) { this.metric = metric; return this; }
        public Builder granularity(String granularity) { this.granularity = granularity; return this; }
        public Builder currentTotal(Double currentTotal) { this.currentTotal = currentTotal; return this; }
        public Builder previousTotal(Double previousTotal) { this.previousTotal = previousTotal; return this; }
        public Builder variationPercentage(Double variationPercentage) { this.variationPercentage = variationPercentage; return this; }
        public Builder points(List<AnalyticsTimelinePointDTO> points) { this.points = points; return this; }

        public AnalyticsTimelineDTO build() {
            return new AnalyticsTimelineDTO(metric, granularity, currentTotal, previousTotal, variationPercentage, points);
        }
    }

    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public String getGranularity() { return granularity; }
    public void setGranularity(String granularity) { this.granularity = granularity; }
    public Double getCurrentTotal() { return currentTotal; }
    public void setCurrentTotal(Double currentTotal) { this.currentTotal = currentTotal; }
    public Double getPreviousTotal() { return previousTotal; }
    public void setPreviousTotal(Double previousTotal) { this.previousTotal = previousTotal; }
    public Double getVariationPercentage() { return variationPercentage; }
    public void setVariationPercentage(Double variationPercentage) { this.variationPercentage = variationPercentage; }
    public List<AnalyticsTimelinePointDTO> getPoints() { return points; }
    public void setPoints(List<AnalyticsTimelinePointDTO> points) { this.points = points; }
}
