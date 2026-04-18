package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.repository.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

    // 🔥 ONLY for NEW contact
    if (contact.getId() == null) {
        contact.setId(UUID.randomUUID().toString());
    }
    return contactRepo.save(contact);
    }

    @Override
    public Contact update(String id) {
        // Implementation for update if needed
        return null;
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Optional<Contact> contact = contactRepo.findById(id);
        contact.ifPresent(contactRepo::delete);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public List<Contact> getFavoriteContacts() {
        return List.of();
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String sortField) {
        Sort sort = sortBy.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }

    // ================== Search Implementation (Name) ==================
    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String sortField) {
        Sort sort = sortBy.equalsIgnoreCase("desc") 
                    ? Sort.by(sortField).descending() 
                    : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByNameContainingIgnoreCase(name, pageable);
    }

    // ================== Search Implementation (Email) ==================
    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String sortField) {
        Sort sort = sortBy.equalsIgnoreCase("desc")
                    ? Sort.by(sortField).descending()
                    : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByEmailContainingIgnoreCase(email, pageable);
    }

    // ================== Search Implementation (Phone) ==================
    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String sortField) {
        Sort sort = sortBy.equalsIgnoreCase("desc")
                    ? Sort.by(sortField).descending()
                    : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByPhoneNumberContainingIgnoreCase(phoneNumber, pageable);
    }

    // ================== Recent Contacts Implementation ==================
    @Override
    public List<Contact> getRecentContacts() {
    return contactRepo.findTop5ByOrderByCreatedAtDesc();
}

    // ================== Monthly Contact Stats Implementation ==================
    @Override
    public List<Object[]> getMonthlyStats() {
    return contactRepo.getMonthlyContactStats();
    }

    

}