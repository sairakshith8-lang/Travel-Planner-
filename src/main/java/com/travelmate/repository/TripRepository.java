package com.travelmate.repository;

import com.travelmate.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Trip> findByIdAndUserId(Long id, Long userId);

    List<Trip> findByUserIdAndReturnDateGreaterThanEqualOrderByDepartureDateAsc(Long userId, LocalDate date);

    List<Trip> findByUserIdAndReturnDateLessThanOrderByDepartureDateDesc(Long userId, LocalDate date);

    boolean existsByIdAndUserId(Long id, Long userId);
}
