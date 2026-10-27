package com.travelmate.repository;

import com.travelmate.entity.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {
    List<ItineraryItem> findByTripIdOrderByDayNumberAscIdAsc(Long tripId);

    Optional<ItineraryItem> findByIdAndTripId(Long id, Long tripId);

    void deleteByTripId(Long tripId);
}
