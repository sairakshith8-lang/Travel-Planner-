package com.travelmate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    @JsonIgnore
    private Trip trip;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "time_of_day", nullable = false, length = 50)
    private String timeOfDay; // Morning, Afternoon, Evening

    @Column(name = "activity_name", nullable = false, length = 200)
    private String activityName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "estimated_cost", nullable = false)
    private Double estimatedCost = 0.0;

    public ItineraryItem() {
    }

    public ItineraryItem(Trip trip, Integer dayNumber, String timeOfDay,
                         String activityName, String description, Double estimatedCost) {
        this.trip = trip;
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
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
