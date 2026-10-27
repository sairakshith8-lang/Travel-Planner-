package com.travelmate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(length = 1000)
    private String description;

    @Column(name = "best_time_to_visit", length = 100)
    private String bestTimeToVisit;

    @Column(name = "estimated_daily_cost", nullable = false)
    private Double estimatedDailyCost = 0.0;

    @Column(name = "travel_type", length = 50)
    private String travelType; // Beach, Heritage, Hill Station, Adventure, Nature, Spiritual

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    private Double rating = 4.8;

    @Column(name = "popular_attractions", length = 500)
    private String popularAttractions;

    public Destination() {
    }

    public Destination(String name, String state, String description,
                       String bestTimeToVisit, Double estimatedDailyCost,
                       String travelType, String imageUrl, Double rating,
                       String popularAttractions) {
        this.name = name;
        this.state = state;
        this.description = description;
        this.bestTimeToVisit = bestTimeToVisit;
        this.estimatedDailyCost = estimatedDailyCost;
        this.travelType = travelType;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.popularAttractions = popularAttractions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBestTimeToVisit() {
        return bestTimeToVisit;
    }

    public void setBestTimeToVisit(String bestTimeToVisit) {
        this.bestTimeToVisit = bestTimeToVisit;
    }

    public Double getEstimatedDailyCost() {
        return estimatedDailyCost;
    }

    public void setEstimatedDailyCost(Double estimatedDailyCost) {
        this.estimatedDailyCost = estimatedDailyCost;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPopularAttractions() {
        return popularAttractions;
    }

    public void setPopularAttractions(String popularAttractions) {
        this.popularAttractions = popularAttractions;
    }
}
