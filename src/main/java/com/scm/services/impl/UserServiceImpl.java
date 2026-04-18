package com.scm.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourcenotFoundException;
import com.scm.repository.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // default role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        logger.info("Saving user with email {}", user.getEmailId());
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        logger.debug("Fetching user by id {}", id);
        return userRepo.findById(id);
    }

@Override
public Optional<User> updateUser(User user) {

    logger.info("Updating user {}", user.getUserId());

    Optional<User> optionalUser = userRepo.findById(user.getUserId());

    if (optionalUser.isEmpty()) {
        return Optional.empty(); // user mila hi nahi
    }

    User existingUser = optionalUser.get(); // ✅ Optional → User

    existingUser.setName(user.getName());
    existingUser.setEmailId(user.getEmailId());
    existingUser.setPassword(user.getPassword());
    existingUser.setPhoneNumber(user.getPhoneNumber());
    existingUser.setAbout(user.getAbout());
    existingUser.setProfileLink(user.getProfileLink());
    existingUser.setEnabled(user.isEnabled());
    existingUser.setEmailVerified(user.isEmailVerified());
    existingUser.setPhoneVerified(user.isPhoneVerified());
    existingUser.setProvider(user.getProvider());
    existingUser.setProviderUserId(user.getProviderUserId());

    User savedUser = userRepo.save(existingUser);

    return Optional.of(savedUser);
}


    @Override
    public void deleteUserById(String id) {
        logger.warn("Deleting user with id {}", id);
        userRepo.deleteById(id);
    }

    @Override
    public boolean isUserExist(String userId) {
        return userRepo.existsById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepo.findAll();
    }

    // ✅ FIXED LINE
    @Override
    public Optional<User> findByEmailId(String emailId) {
        return userRepo.findByEmailId(emailId);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmailId(email).orElse(null);
    }
}
