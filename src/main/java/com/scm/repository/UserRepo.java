package com.scm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

    Optional<User> findByEmailId(String emailId);

    //extra method db related
    //custom query method
    //custom finder methods

    
}
