package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.entity.Destination;
import com.travelmate.service.DestinationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Destination>>> getDestinations(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String travelType,
            @RequestParam(required = false) Double maxCost) {
        List<Destination> destinations = destinationService.searchDestinations(query, travelType, maxCost);
        return ResponseEntity.ok(ApiResponse.ok("Destinations retrieved successfully", destinations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Destination>> getDestinationById(@PathVariable Long id) {
        Destination destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(ApiResponse.ok("Destination found", destination));
    }
}
