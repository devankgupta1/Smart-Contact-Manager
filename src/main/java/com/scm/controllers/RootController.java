package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.scm.entities.User;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        // ❌ anonymous user ignore
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return;
        }

        Object principal = authentication.getPrincipal();

        // ❌ agar UserDetails nahi hai → ignore
        if (!(principal instanceof UserDetails)) {
            return;
        }

        String email = ((UserDetails) principal).getUsername();
        logger.info("User logged in: {}", email);

        User user = userService.getUserByEmail(email);

        model.addAttribute("LoggedInUser", user);
    }
}