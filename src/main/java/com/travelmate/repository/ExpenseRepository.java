package com.travelmate.repository;

import com.travelmate.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripIdOrderByIdAsc(Long tripId);

    Optional<Expense> findByIdAndTripId(Long id, Long tripId);

    void deleteByTripId(Long tripId);
}
