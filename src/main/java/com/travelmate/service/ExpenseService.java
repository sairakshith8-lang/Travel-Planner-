package com.travelmate.service;

import com.travelmate.dto.ExpenseDTO;
import com.travelmate.entity.Expense;
import com.travelmate.entity.Trip;
import com.travelmate.entity.User;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.exception.UnauthorizedException;
import com.travelmate.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDTO> getExpensesForTrip(Trip trip) {
        return expenseRepository.findByTripIdOrderByIdAsc(trip.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Expense> generateInitialExpenses(Trip trip) {
        double budget = trip.getBudget() != null ? trip.getBudget() : 10000.0;
        int travelers = trip.getTravelers() != null ? trip.getTravelers() : 1;

        // Proportional initial budget allocation:
        // Transportation: 25%, Accommodation: 35%, Food: 20%, Sightseeing: 12%, Shopping & Misc: 8%
        double transportEst = Math.round(budget * 0.25);
        double accomEst = Math.round(budget * 0.35);
        double foodEst = Math.round(budget * 0.20);
        double sightEst = Math.round(budget * 0.12);
        double shopEst = Math.round(budget * 0.08);

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(trip, "Transportation", transportEst, 0.0,
                "Flights/Train/Bus & Local taxi/cabs for " + travelers + " traveler(s)"));
        expenses.add(new Expense(trip, "Accommodation", accomEst, 0.0,
                "Hotels/Resort stay booking"));
        expenses.add(new Expense(trip, "Food", foodEst, 0.0,
                "Daily meals, breakfast, authentic local restaurants & cafes"));
        expenses.add(new Expense(trip, "Sightseeing and activities", sightEst, 0.0,
                "Entry tickets, adventure sports, guided tours & attractions"));
        expenses.add(new Expense(trip, "Shopping and miscellaneous", shopEst, 0.0,
                "Souvenirs, emergency fund, and miscellaneous expenses"));

        return expenseRepository.saveAll(expenses);
    }

    @Transactional
    public ExpenseDTO addExpense(Trip trip, ExpenseDTO dto, User user) {
        verifyTripOwnership(trip, user);

        Expense expense = new Expense(
                trip,
                dto.getCategory().trim(),
                dto.getEstimatedAmount() != null ? dto.getEstimatedAmount() : 0.0,
                dto.getActualAmount() != null ? dto.getActualAmount() : 0.0,
                dto.getDescription() != null ? dto.getDescription().trim() : ""
        );

        Expense saved = expenseRepository.save(expense);
        return toDTO(saved);
    }

    @Transactional
    public ExpenseDTO updateExpense(Trip trip, Long expenseId, ExpenseDTO dto, User user) {
        verifyTripOwnership(trip, user);

        Expense expense = expenseRepository.findByIdAndTripId(expenseId, trip.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));

        if (dto.getCategory() != null) expense.setCategory(dto.getCategory().trim());
        if (dto.getEstimatedAmount() != null) expense.setEstimatedAmount(dto.getEstimatedAmount());
        if (dto.getActualAmount() != null) expense.setActualAmount(dto.getActualAmount());
        if (dto.getDescription() != null) expense.setDescription(dto.getDescription().trim());

        Expense updated = expenseRepository.save(expense);
        return toDTO(updated);
    }

    @Transactional
    public void deleteExpense(Trip trip, Long expenseId, User user) {
        verifyTripOwnership(trip, user);

        Expense expense = expenseRepository.findByIdAndTripId(expenseId, trip.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + expenseId));

        expenseRepository.delete(expense);
    }

    private void verifyTripOwnership(Trip trip, User user) {
        if (!trip.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this trip's expenses.");
        }
    }

    public ExpenseDTO toDTO(Expense expense) {
        return new ExpenseDTO(
                expense.getId(),
                expense.getTrip().getId(),
                expense.getCategory(),
                expense.getEstimatedAmount(),
                expense.getActualAmount(),
                expense.getDescription()
        );
    }
}
