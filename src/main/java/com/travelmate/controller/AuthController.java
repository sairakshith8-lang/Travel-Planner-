package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.dto.LoginRequest;
import com.travelmate.dto.RegisterRequest;
import com.travelmate.dto.UserDTO;
import com.travelmate.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        UserDTO user = userService.register(request, session);
        return new ResponseEntity<>(ApiResponse.ok("Registration successful! Welcome to TravelMate.", user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        UserDTO user = userService.login(request, session);
        return ResponseEntity.ok(ApiResponse.ok("Logged in successfully.", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(HttpSession session) {
        if (!userService.isLoggedIn(session)) {
            return new ResponseEntity<>(ApiResponse.error("Not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        UserDTO user = userService.getCurrentUser(session);
        return ResponseEntity.ok(ApiResponse.ok("Authenticated user", user));
    }
}
