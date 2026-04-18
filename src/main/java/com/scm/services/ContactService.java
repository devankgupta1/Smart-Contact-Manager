package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    // save contact
    Contact save(Contact contact);

    // update contact
    Contact update(String id);

    // get contact by id
    List<Contact> getAll();

    // get contact by id
    Contact getById(String id);

    // delete contact by id
    void delete(String id);

    //get contacts of a user by searching with name
    Page<Contact> searchByName(String name, int page, int size, String sortBy, String sortField);

    //search contacts of a user by email
    Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String sortField);

    //search contacts of a user by phone number
    Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String sortField);

    //get by user id
    List<Contact> getByUserId(String userId);

    // get all favorite contacts of a user
    List<Contact> getFavoriteContacts();

    // get contacts of a user with pagination
    Page<Contact> getByUser(User user, int page, int size, String sortBy, String sortField);

    // get recent contacts
    public List<Contact> getRecentContacts();

    // get monthly contact stats
    public List<Object[]> getMonthlyStats();

}