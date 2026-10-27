package com.travelmate.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseDTO {

    private Long id;
    private Long tripId;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Estimated amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Estimated amount must be non-negative")
    private Double estimatedAmount = 0.0;

    @NotNull(message = "Actual amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Actual amount must be non-negative")
    private Double actualAmount = 0.0;

    private String description;

    public ExpenseDTO() {
    }

    public ExpenseDTO(Long id, Long tripId, String category, Double estimatedAmount, Double actualAmount, String description) {
        this.id = id;
        this.tripId = tripId;
        this.category = category;
        this.estimatedAmount = estimatedAmount != null ? estimatedAmount : 0.0;
        this.actualAmount = actualAmount != null ? actualAmount : 0.0;
        this.description = description;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(Double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
