package com.scm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;
@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // 1. Get all contacts of a specific user with pagination
    Page<Contact> findByUser(User user, Pageable pageable);

    // 2. Custom query for userId
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    // 3. SEARCH METHODS (Parameters ke sath match karne ke liye 'UserAnd' hata diya hai)
    
    // Name Search
    Page<Contact> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Email Search
    Page<Contact> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    
    // Phone Search
    Page<Contact> findByPhoneNumberContainingIgnoreCase(String phoneNumber, Pageable pageable);

    // Recent Contacts
    List<Contact> findTop5ByOrderByCreatedAtDesc();

    // Monthly Contact Stats
    @Query("""
    SELECT MONTH(c.createdAt), COUNT(c)
    FROM Contact c
    GROUP BY MONTH(c.createdAt)
    ORDER BY MONTH(c.createdAt)
    """)
    List<Object[]> getMonthlyContactStats();

}
