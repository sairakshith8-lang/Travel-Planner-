package com.travelmate;

import com.travelmate.dto.LoginRequest;
import com.travelmate.dto.RegisterRequest;
import com.travelmate.dto.UserDTO;
import com.travelmate.exception.BadRequestException;
import com.travelmate.exception.UnauthorizedException;
import com.travelmate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("Priya Patel", "priya.test@travelmate.com", "secret123");
        UserDTO user = userService.register(request, session);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("Priya Patel", user.getName());
        assertEquals("priya.test@travelmate.com", user.getEmail());
        assertTrue(userService.isLoggedIn(session));
    }

    @Test
    void testRegisterDuplicateEmailThrowsException() {
        RegisterRequest req1 = new RegisterRequest("User One", "dup@travelmate.com", "pass123");
        userService.register(req1, session);

        RegisterRequest req2 = new RegisterRequest("User Two", "dup@travelmate.com", "pass456");
        assertThrows(BadRequestException.class, () -> userService.register(req2, session));
    }

    @Test
    void testLoginSuccess() {
        RegisterRequest req = new RegisterRequest("Vikram Rao", "vikram@travelmate.com", "mypassword");
        userService.register(req, session);

        MockHttpSession newSession = new MockHttpSession();
        LoginRequest loginReq = new LoginRequest("vikram@travelmate.com", "mypassword");
        UserDTO loggedInUser = userService.login(loginReq, newSession);

        assertNotNull(loggedInUser);
        assertEquals("Vikram Rao", loggedInUser.getName());
        assertTrue(userService.isLoggedIn(newSession));
    }

    @Test
    void testLoginInvalidPassword() {
        RegisterRequest req = new RegisterRequest("Vikram Rao", "vikram2@travelmate.com", "mypassword");
        userService.register(req, session);

        MockHttpSession newSession = new MockHttpSession();
        LoginRequest loginReq = new LoginRequest("vikram2@travelmate.com", "wrongpassword");
        assertThrows(UnauthorizedException.class, () -> userService.login(loginReq, newSession));
    }
}
