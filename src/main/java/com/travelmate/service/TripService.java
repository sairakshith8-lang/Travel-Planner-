package com.travelmate.service;

import com.travelmate.dto.ExpenseDTO;
import com.travelmate.dto.ItineraryItemDTO;
import com.travelmate.dto.TripRequest;
import com.travelmate.dto.TripResponse;
import com.travelmate.entity.Trip;
import com.travelmate.entity.User;
import com.travelmate.exception.BadRequestException;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.exception.UnauthorizedException;
import com.travelmate.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final ItineraryService itineraryService;
    private final ExpenseService expenseService;

    public TripService(TripRepository tripRepository,
                       ItineraryService itineraryService,
                       ExpenseService expenseService) {
        this.tripRepository = tripRepository;
        this.itineraryService = itineraryService;
        this.expenseService = expenseService;
    }

    @Transactional
    public TripResponse createTrip(TripRequest request, User user) {
        validateTripDates(request.getDepartureDate(), request.getReturnDate());

        Trip trip = new Trip(
                user,
                request.getTripName().trim(),
                request.getSource().trim(),
                request.getDestination().trim(),
                request.getDepartureDate(),
                request.getReturnDate(),
                request.getTravelers(),
                request.getBudget(),
                request.getTravelPreference() != null ? request.getTravelPreference().trim() : "Relaxation"
        );

        Trip savedTrip = tripRepository.save(trip);

        // Auto-generate initial smart itinerary and budget breakdown
        itineraryService.generateInitialItinerary(savedTrip);
        expenseService.generateInitialExpenses(savedTrip);

        return getTripDetails(savedTrip.getId(), user);
    }

    public List<TripResponse> getMyTrips(User user, String filter) {
        List<Trip> trips;
        LocalDate today = LocalDate.now();

        if ("upcoming".equalsIgnoreCase(filter)) {
            trips = tripRepository.findByUserIdAndReturnDateGreaterThanEqualOrderByDepartureDateAsc(user.getId(), today);
        } else if ("completed".equalsIgnoreCase(filter)) {
            trips = tripRepository.findByUserIdAndReturnDateLessThanOrderByDepartureDateDesc(user.getId(), today);
        } else {
            trips = tripRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        }

        return trips.stream()
                .map(this::toResponseSummary)
                .collect(Collectors.toList());
    }

    public TripResponse getTripDetails(Long tripId, User user) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found or you do not have permission to access it."));

        return toFullResponse(trip);
    }

    public Trip getTripEntityForUser(Long tripId, User user) {
        return tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found or you do not have permission to access it."));
    }

    @Transactional
    public TripResponse updateTrip(Long tripId, TripRequest request, User user) {
        validateTripDates(request.getDepartureDate(), request.getReturnDate());

        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        trip.setTripName(request.getTripName().trim());
        trip.setSource(request.getSource().trim());
        trip.setDestination(request.getDestination().trim());
        trip.setDepartureDate(request.getDepartureDate());
        trip.setReturnDate(request.getReturnDate());
        trip.setTravelers(request.getTravelers());
        trip.setBudget(request.getBudget());
        if (request.getTravelPreference() != null) {
            trip.setTravelPreference(request.getTravelPreference().trim());
        }

        Trip updated = tripRepository.save(trip);
        return toFullResponse(updated);
    }

    @Transactional
    public void deleteTrip(Long tripId, User user) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found or you do not have permission to delete it."));

        tripRepository.delete(trip);
    }

    private void validateTripDates(LocalDate departure, LocalDate returnDate) {
        if (returnDate.isBefore(departure)) {
            throw new BadRequestException("Return date cannot be earlier than departure date.");
        }
    }

    public TripResponse toResponseSummary(Trip trip) {
        TripResponse res = new TripResponse();
        res.setId(trip.getId());
        res.setTripName(trip.getTripName());
        res.setSource(trip.getSource());
        res.setDestination(trip.getDestination());
        res.setDepartureDate(trip.getDepartureDate());
        res.setReturnDate(trip.getReturnDate());
        res.setTravelers(trip.getTravelers());
        res.setBudget(trip.getBudget());
        res.setTravelPreference(trip.getTravelPreference());

        long days = ChronoUnit.DAYS.between(trip.getDepartureDate(), trip.getReturnDate()) + 1;
        res.setDurationDays(days > 0 ? days : 1);

        LocalDate today = LocalDate.now();
        if (today.isBefore(trip.getDepartureDate())) {
            res.setStatus("Upcoming");
        } else if (!today.isAfter(trip.getReturnDate())) {
            res.setStatus("Active");
        } else {
            res.setStatus("Completed");
        }

        // Summary calculations
        List<ExpenseDTO> expenses = expenseService.getExpensesForTrip(trip);
        double totalEstimated = expenses.stream().mapToDouble(ExpenseDTO::getEstimatedAmount).sum();
        double totalActual = expenses.stream().mapToDouble(ExpenseDTO::getActualAmount).sum();

        res.setTotalEstimatedCost(totalEstimated);
        res.setTotalActualSpent(totalActual);
        res.setRemainingBudget(trip.getBudget() - totalActual);

        return res;
    }

    public TripResponse toFullResponse(Trip trip) {
        TripResponse res = toResponseSummary(trip);
        res.setItineraryItems(itineraryService.getItineraryForTrip(trip));
        res.setExpenses(expenseService.getExpensesForTrip(trip));
        return res;
    }
}
