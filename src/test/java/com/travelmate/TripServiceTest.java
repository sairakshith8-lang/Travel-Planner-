package com.travelmate;

import com.travelmate.dto.RegisterRequest;
import com.travelmate.dto.TripRequest;
import com.travelmate.dto.TripResponse;
import com.travelmate.dto.UserDTO;
import com.travelmate.entity.User;
import com.travelmate.exception.BadRequestException;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.service.TripService;
import com.travelmate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockHttpSession session1 = new MockHttpSession();
        UserDTO dto1 = userService.register(new RegisterRequest("User One", "user1@test.com", "pass12345"), session1);
        user1 = userService.getCurrentUserEntity(session1);

        MockHttpSession session2 = new MockHttpSession();
        UserDTO dto2 = userService.register(new RegisterRequest("User Two", "user2@test.com", "pass12345"), session2);
        user2 = userService.getCurrentUserEntity(session2);
    }

    @Test
    void testCreateTripGeneratesItineraryAndExpenses() {
        TripRequest request = new TripRequest(
                "Manali Snow Adventure",
                "Delhi",
                "Manali",
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(9),
                2,
                30000.0,
                "Adventure"
        );

        TripResponse trip = tripService.createTrip(request, user1);

        assertNotNull(trip);
        assertNotNull(trip.getId());
        assertEquals("Manali Snow Adventure", trip.getTripName());
        assertEquals(5, trip.getDurationDays()); // departure + 4 days = 5 days total
        assertFalse(trip.getItineraryItems().isEmpty(), "Itinerary items should be auto-generated");
        assertFalse(trip.getExpenses().isEmpty(), "Expenses should be auto-allocated");
        assertTrue(trip.getTotalEstimatedCost() > 0, "Total estimated cost should be computed");
    }

    @Test
    void testCreateTripInvalidDatesThrowsException() {
        TripRequest request = new TripRequest(
                "Invalid Trip",
                "Delhi",
                "Goa",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(5), // return before departure!
                1,
                15000.0,
                "Relaxation"
        );

        assertThrows(BadRequestException.class, () -> tripService.createTrip(request, user1));
    }

    @Test
    void testTripPrivacyUserCannotAccessOtherUsersTrip() {
        TripRequest request = new TripRequest(
                "Private Trip",
                "Bangalore",
                "Kerala",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(6),
                2,
                25000.0,
                "Nature"
        );

        TripResponse trip1 = tripService.createTrip(request, user1);

        // User 2 should NOT be able to view user 1's trip
        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripDetails(trip1.getId(), user2));

        // User 2's trip list should not include User 1's trip
        List<TripResponse> user2Trips = tripService.getMyTrips(user2, "all");
        assertTrue(user2Trips.stream().noneMatch(t -> t.getId().equals(trip1.getId())));
    }
}
