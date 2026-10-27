package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.dto.ItineraryItemDTO;
import com.travelmate.entity.Trip;
import com.travelmate.entity.User;
import com.travelmate.service.ItineraryService;
import com.travelmate.service.TripService;
import com.travelmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final TripService tripService;
    private final UserService userService;

    public ItineraryController(ItineraryService itineraryService,
                               TripService tripService,
                               UserService userService) {
        this.itineraryService = itineraryService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItineraryItemDTO>>> getItinerary(
            @PathVariable Long tripId,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        List<ItineraryItemDTO> items = itineraryService.getItineraryForTrip(trip);
        return ResponseEntity.ok(ApiResponse.ok("Itinerary items retrieved successfully", items));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItineraryItemDTO>> addActivity(
            @PathVariable Long tripId,
            @Valid @RequestBody ItineraryItemDTO dto,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        ItineraryItemDTO created = itineraryService.addItem(trip, dto, user);
        return new ResponseEntity<>(ApiResponse.ok("Activity added to itinerary successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItineraryItemDTO>> updateActivity(
            @PathVariable Long tripId,
            @PathVariable Long itemId,
            @Valid @RequestBody ItineraryItemDTO dto,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        ItineraryItemDTO updated = itineraryService.updateItem(trip, itemId, dto, user);
        return ResponseEntity.ok(ApiResponse.ok("Activity updated successfully", updated));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteActivity(
            @PathVariable Long tripId,
            @PathVariable Long itemId,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        itineraryService.deleteItem(trip, itemId, user);
        return ResponseEntity.ok(ApiResponse.ok("Activity removed from itinerary."));
    }
}
