package com.travelmate.controller;

import com.travelmate.dto.ApiResponse;
import com.travelmate.dto.ContactRequest;
import com.travelmate.entity.ContactMessage;
import com.travelmate.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ContactMessage>> submitContact(@Valid @RequestBody ContactRequest request) {
        ContactMessage saved = contactService.saveMessage(request);
        return new ResponseEntity<>(ApiResponse.ok("Thank you for reaching out! Our travel team will respond promptly.", saved), HttpStatus.CREATED);
    }
}
