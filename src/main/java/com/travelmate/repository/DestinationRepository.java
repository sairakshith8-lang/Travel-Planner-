package com.travelmate.repository;

import com.travelmate.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByNameContainingIgnoreCase(String name);

    List<Destination> findByTravelTypeIgnoreCase(String travelType);

    @Query("SELECT d FROM Destination d WHERE " +
           "(:query IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.state) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:travelType IS NULL OR :travelType = '' OR LOWER(d.travelType) = LOWER(:travelType)) AND " +
           "(:maxCost IS NULL OR d.estimatedDailyCost <= :maxCost)")
    List<Destination> searchDestinations(@Param("query") String query,
                                         @Param("travelType") String travelType,
                                         @Param("maxCost") Double maxCost);
}
