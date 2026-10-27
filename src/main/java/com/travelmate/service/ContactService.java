package com.travelmate.service;

import com.travelmate.dto.ContactRequest;
import com.travelmate.entity.ContactMessage;
import com.travelmate.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @Transactional
    public ContactMessage saveMessage(ContactRequest request) {
        ContactMessage message = new ContactMessage(
                request.getName().trim(),
                request.getEmail().trim(),
                request.getSubject() != null ? request.getSubject().trim() : "General Inquiry",
                request.getMessage().trim()
        );
        return contactMessageRepository.save(message);
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAll();
    }
}
