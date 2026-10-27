package com.travelmate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "trip_name", nullable = false, length = 150)
    private String tripName;

    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @Column(name = "destination", nullable = false, length = 100)
    private String destination;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name = "travelers", nullable = false)
    private Integer travelers = 1;

    @Column(name = "budget", nullable = false)
    private Double budget = 0.0;

    @Column(name = "travel_preference", length = 50)
    private String travelPreference;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC, id ASC")
    private List<ItineraryItem> itineraryItems = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Expense> expenses = new ArrayList<>();

    public Trip() {
    }

    public Trip(User user, String tripName, String source, String destination,
                LocalDate departureDate, LocalDate returnDate, Integer travelers,
                Double budget, String travelPreference) {
        this.user = user;
        this.tripName = tripName;
        this.source = source;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.travelers = travelers;
        this.budget = budget;
        this.travelPreference = travelPreference;
        this.createdAt = LocalDateTime.now();
    }

    // Convenience helper methods
    public void addItineraryItem(ItineraryItem item) {
        itineraryItems.add(item);
        item.setTrip(this);
    }

    public void removeItineraryItem(ItineraryItem item) {
        itineraryItems.remove(item);
        item.setTrip(null);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.setTrip(this);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.setTrip(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ItineraryItem> getItineraryItems() {
        return itineraryItems;
    }

    public void setItineraryItems(List<ItineraryItem> itineraryItems) {
        this.itineraryItems = itineraryItems;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
