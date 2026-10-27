package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.dto.TripRequest;
import com.travelmate.dto.TripResponse;
import com.travelmate.entity.User;
import com.travelmate.service.TripService;
import com.travelmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripResponse>> createTrip(
            @Valid @RequestBody TripRequest request,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        TripResponse trip = tripService.createTrip(request, user);
        return new ResponseEntity<>(ApiResponse.ok("Trip created successfully with smart itinerary and budget plan!", trip), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TripResponse>>> getMyTrips(
            @RequestParam(required = false, defaultValue = "all") String filter,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        List<TripResponse> trips = tripService.getMyTrips(user, filter);
        return ResponseEntity.ok(ApiResponse.ok("Trips retrieved successfully", trips));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> getTripDetails(
            @PathVariable Long id,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        TripResponse trip = tripService.getTripDetails(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Trip details retrieved successfully", trip));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TripResponse>> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequest request,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        TripResponse updated = tripService.updateTrip(id, request, user);
        return ResponseEntity.ok(ApiResponse.ok("Trip updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @PathVariable Long id,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        tripService.deleteTrip(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Trip deleted successfully."));
    }
}
