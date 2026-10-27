package com.travelmate.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TripRequest {

    @NotBlank(message = "Trip name is required")
    @Size(max = 150, message = "Trip name cannot exceed 150 characters")
    private String tripName;

    @NotBlank(message = "Starting location is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    @NotNull(message = "Number of travelers is required")
    @Min(value = 1, message = "There must be at least 1 traveler")
    private Integer travelers;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Budget must be non-negative")
    private Double budget;

    private String travelPreference;

    public TripRequest() {
    }

    public TripRequest(String tripName, String source, String destination,
                       LocalDate departureDate, LocalDate returnDate,
                       Integer travelers, Double budget, String travelPreference) {
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.travelers = travelers;
        this.budget = budget;
        this.travelPreference = travelPreference;
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
}
