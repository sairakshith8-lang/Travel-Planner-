package com.travelmate.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripResponse {
    private Long id;
    private String tripName;
    private String source;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer travelers;
    private Double budget;
    private String travelPreference;
    private String status;
    private long durationDays;
    private Double totalEstimatedCost = 0.0;
    private Double totalActualSpent = 0.0;
    private Double remainingBudget = 0.0;
    private List<ItineraryItemDTO> itineraryItems = new ArrayList<>();
    private List<ExpenseDTO> expenses = new ArrayList<>();

    public TripResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getTravelers() {
        return travelers;
    }

    public void setTravelers(Integer travelers) {
        this.travelers = travelers;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getTravelPreference() {
        return travelPreference;
    }

    public void setTravelPreference(String travelPreference) {
        this.travelPreference = travelPreference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(long durationDays) {
        this.durationDays = durationDays;
    }

    public Double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    public void setTotalEstimatedCost(Double totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public Double getTotalActualSpent() {
        return totalActualSpent;
    }

    public void setTotalActualSpent(Double totalActualSpent) {
        this.totalActualSpent = totalActualSpent;
    }

    public Double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public List<ItineraryItemDTO> getItineraryItems() {
        return itineraryItems;
    }

    public void setItineraryItems(List<ItineraryItemDTO> itineraryItems) {
        this.itineraryItems = itineraryItems;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }
}
