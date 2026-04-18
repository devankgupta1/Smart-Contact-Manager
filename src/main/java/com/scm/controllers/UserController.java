package com.scm.controllers;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;

// user dashboard controller
@Controller 
@RequestMapping("/user")
public class UserController {

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        String UserName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", UserName);

        User user = userService.getUserByEmail(UserName);

        logger.info("LoggedInUser", user.getName());
        logger.info("LoggedInUser", user.getEmailId());

        model.addAttribute("user", user);

    }

    @Autowired
    private UserService userService; // Assuming you have a UserService to handle user-related operations

    @Autowired
    private ContactService contactService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

@RequestMapping("/dashboard")
public String userDashboard(Model model, Authentication authentication) {

    String UserName = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(UserName);

    model.addAttribute("LoggedInUser", user);

    // 🔥 recent contacts (FIX)
    model.addAttribute("contacts", contactService.getRecentContacts());

    // graph data
    model.addAttribute("chartData", contactService.getMonthlyStats());

    // ADD THIS
    List<Contact> contacts = contactService.getByUserId(user.getUserId());

    model.addAttribute("totalContacts", contacts.size());

    long favoriteCount = contacts.stream()
            .filter(Contact::isFavorite)
            .count();

    model.addAttribute("favoriteContacts", favoriteCount);

    return "user/dashboard";
}

    // User profile
@RequestMapping("/profile")
public String userProfile(Model model, Authentication authentication) {

    String username = Helper.getEmailOfLoggedInUser(authentication);
    logger.info("User logged in: {}", username);

    // 🔥 DB se user lao (IMPORTANT)
    User user = userService.getUserByEmail(username);

    model.addAttribute("LoggedInUser", user);

    return "user/profile";
}

}
