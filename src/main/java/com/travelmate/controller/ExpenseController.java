package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.dto.ExpenseDTO;
import com.travelmate.entity.Trip;
import com.travelmate.entity.User;
import com.travelmate.service.ExpenseService;
import com.travelmate.service.TripService;
import com.travelmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final TripService tripService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService,
                             TripService tripService,
                             UserService userService) {
        this.expenseService = expenseService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getExpenses(
            @PathVariable Long tripId,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        List<ExpenseDTO> expenses = expenseService.getExpensesForTrip(trip);
        return ResponseEntity.ok(ApiResponse.ok("Expenses retrieved successfully", expenses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> addExpense(
            @PathVariable Long tripId,
            @Valid @RequestBody ExpenseDTO dto,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        ExpenseDTO created = expenseService.addExpense(trip, dto, user);
        return new ResponseEntity<>(ApiResponse.ok("Expense added successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(
            @PathVariable Long tripId,
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseDTO dto,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        ExpenseDTO updated = expenseService.updateExpense(trip, expenseId, dto, user);
        return ResponseEntity.ok(ApiResponse.ok("Expense updated successfully", updated));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable Long tripId,
            @PathVariable Long expenseId,
            HttpSession session) {
        User user = userService.getCurrentUserEntity(session);
        Trip trip = tripService.getTripEntityForUser(tripId, user);
        expenseService.deleteExpense(trip, expenseId, user);
        return ResponseEntity.ok(ApiResponse.ok("Expense deleted successfully."));
    }
}
