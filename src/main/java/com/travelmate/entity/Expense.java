package com.travelmate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(nullable = false, length = 50)
    private String category; // Transportation, Accommodation, Food, Sightseeing, Shopping

    @Column(name = "estimated_amount", nullable = false)
    private Double estimatedAmount = 0.0;

    @Column(name = "actual_amount", nullable = false)
    private Double actualAmount = 0.0;

    @Column(length = 500)
    private String description;

    public Expense() {
    }

    public Expense(Trip trip, String category, Double estimatedAmount, Double actualAmount, String description) {
        this.trip = trip;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
