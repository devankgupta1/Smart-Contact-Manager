package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.User;

public interface UserService
{

    User saveUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);

    void deleteUserById(String id);

    boolean isUserExist(String userId);

    Optional<User> findByEmailId(String emailId); 

    List<User> getAllUsers();

    User getUserByEmail(String email);

}
