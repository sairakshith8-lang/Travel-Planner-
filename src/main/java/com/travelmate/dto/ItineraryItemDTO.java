package com.travelmate.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItineraryItemDTO {

    private Long id;
    private Long tripId;

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    private Integer dayNumber;

    @NotBlank(message = "Time of day is required (e.g., Morning, Afternoon, Evening)")
    private String timeOfDay;

    @NotBlank(message = "Activity name is required")
    private String activityName;

    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated cost must be non-negative")
    private Double estimatedCost = 0.0;

    public ItineraryItemDTO() {
    }

    public ItineraryItemDTO(Long id, Long tripId, Integer dayNumber, String timeOfDay,
                            String activityName, String description, Double estimatedCost) {
        this.id = id;
        this.tripId = tripId;
        this.dayNumber = dayNumber;
        this.timeOfDay = timeOfDay;
        this.activityName = activityName;
        this.description = description;
        this.estimatedCost = estimatedCost != null ? estimatedCost : 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}
