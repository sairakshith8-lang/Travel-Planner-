package com.travelmate.service;

import com.travelmate.dto.LoginRequest;
import com.travelmate.dto.RegisterRequest;
import com.travelmate.dto.UserDTO;
import com.travelmate.entity.User;
import com.travelmate.exception.BadRequestException;
import com.travelmate.exception.ResourceNotFoundException;
import com.travelmate.exception.UnauthorizedException;
import com.travelmate.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static final String SESSION_USER_ID = "LOGGED_IN_USER_ID";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO register(RegisterRequest request, HttpSession session) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("An account with this email already exists.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getName().trim(), email, encodedPassword);
        User savedUser = userRepository.save(user);

        // Auto login upon registration
        session.setAttribute(SESSION_USER_ID, savedUser.getId());

        return toDTO(savedUser);
    }

    public UserDTO login(LoginRequest request, HttpSession session) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password.");
        }

        session.setAttribute(SESSION_USER_ID, user.getId());
        return toDTO(user);
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_ID);
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
        }
    }

    public User getCurrentUserEntity(HttpSession session) {
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            throw new UnauthorizedException("Please log in to continue.");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User session expired or user not found."));
    }

    public UserDTO getCurrentUser(HttpSession session) {
        return toDTO(getCurrentUserEntity(session));
    }

    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER_ID) != null;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
