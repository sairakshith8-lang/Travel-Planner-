package com.travelmate.config;

import com.travelmate.dto.TripRequest;
import com.travelmate.entity.User;
import com.travelmate.repository.TripRepository;
import com.travelmate.repository.UserRepository;
import com.travelmate.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripService tripService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TripRepository tripRepository,
                           TripService tripService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.tripService = tripService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed default demo user for effortless presentation and testing
        String demoEmail = "demo@travelmate.com";
        if (!userRepository.existsByEmail(demoEmail)) {
            User demoUser = new User("Aarav Sharma", demoEmail, passwordEncoder.encode("password123"));
            demoUser = userRepository.save(demoUser);
            log.info("Demo user created: {} / password123", demoEmail);

            // Create initial sample trip for the demo user
            if (tripRepository.findByUserIdOrderByCreatedAtDesc(demoUser.getId()).isEmpty()) {
                TripRequest sampleTrip = new TripRequest(
                        "Goa Coastal Getaway 2026",
                        "Mumbai",
                        "Goa",
                        LocalDate.now().plusDays(10),
                        LocalDate.now().plusDays(14),
                        2,
                        45000.0,
                        "Relaxation"
                );
                tripService.createTrip(sampleTrip, demoUser);
                log.info("Sample trip initialized for demo user.");
            }
        }
    }
}
