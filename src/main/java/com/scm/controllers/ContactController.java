package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.validation.Valid;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.Helper;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService  contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService; // Assuming you have a UserService to handle user-related operations

    
    @RequestMapping("/add")
    public String contact(Model model, Authentication authentication) {


        String UserName = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", UserName);

        User user = userService.getUserByEmail(UserName);

        logger.info("LoggedInUser", user.getName());
        logger.info("LoggedInUser", user.getEmailId());

        model.addAttribute("LoggedInUser", user);

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @PostMapping("/save")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, Model model) {

        //validation errors
        if (result.hasErrors()) {

        System.out.println("ERRORS: " + result.getAllErrors());

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        model.addAttribute("LoggedInUser", user);
        model.addAttribute("contactForm", contactForm); // 🔥 VERY IMPORTANT

        return "user/add_contact";
}

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        // ✅ safe image upload
        String fileURL = null;

        if (contactForm.getContactImage() != null &&
         !contactForm.getContactImage().isEmpty()) {

        logger.info("Contact Image: {}", contactForm.getContactImage().getOriginalFilename());

         fileURL = imageService.uploadImage(contactForm.getContactImage());
                } else {
                logger.warn("No image uploaded");
}


        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setUser(user);
        contact.setDescription(contactForm.getDescription());
        contact.setProfilePicture(fileURL);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        

        contactService.save(contact);

        System.out.println("Contact Form Data: " + contactForm);
        return "redirect:/user/contact/add";
    }


// @GetMapping({"", "/search"})
// public String viewContacts(
//         @RequestParam(defaultValue = "0") int page,
//         @RequestParam(value = "size", defaultValue = "3") int size,
//         @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
//         @RequestParam(value = "direction", defaultValue = "asc") String direction,
//         @RequestParam(value = "field", required = false) String field,
//         @RequestParam(value = "keyword", required = false) String keyword,
//         Model model,
//         Authentication authentication) {

//     String email = Helper.getEmailOfLoggedInUser(authentication);
//     User user = userService.getUserByEmail(email);

//     model.addAttribute("LoggedInUser", user);

//     Page<Contact> pageContact;

//     // 🔍 SEARCH LOGIC
//     if (field != null && keyword != null && !field.isEmpty() && !keyword.isEmpty()) {

//         if (field.equalsIgnoreCase("name")) {
//             pageContact = contactService.searchByName(keyword, page, size, direction, "name");

//         } else if (field.equalsIgnoreCase("email")) {
//             pageContact = contactService.searchByEmail(keyword, page, size, direction, "email");

//         } else if (field.equalsIgnoreCase("phone")) {
//             pageContact = contactService.searchByPhoneNumber(keyword, page, size, direction, "phoneNumber");

//         } else {
//             pageContact = contactService.getByUser(user, page, size, direction, sortBy);
//         }

//         model.addAttribute("pageContact", pageContact);

//         return "user/search_contacts";   // 🔍 search page
//     } 

//     // 📄 NORMAL CONTACT LIST
//     pageContact = contactService.getByUser(user, page, size, direction, sortBy);

//     model.addAttribute("pageContact", pageContact);

//     return "user/view_contacts";   // 📄 normal page
// }


@GetMapping("")
public String viewContacts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "3") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,
        Model model,
        Authentication authentication) {

    String email = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(email);

    model.addAttribute("LoggedInUser", user);

    Page<Contact> pageContact =
        contactService.getByUser(user, page, size, direction, sortBy);

    model.addAttribute("pageContact", pageContact);

    return "user/view_contacts";   // ✅ yahi rahega
}

@GetMapping("/search")
public String searchContacts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "3") int size,
        @RequestParam(value = "field") String field,
        @RequestParam(value = "keyword") String keyword,
        Model model,
        Authentication authentication) {

    String email = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(email);

    model.addAttribute("LoggedInUser", user);

    Page<Contact> pageContact;

    if (field.equalsIgnoreCase("name")) {
        pageContact = contactService.searchByName(keyword, page, size, "asc", "name");

    } else if (field.equalsIgnoreCase("email")) {
        pageContact = contactService.searchByEmail(keyword, page, size, "asc", "email");

    } else if (field.equalsIgnoreCase("phone")) {
        pageContact = contactService.searchByPhoneNumber(keyword, page, size, "asc", "phoneNumber");

    } else {
        pageContact = contactService.getByUser(user, page, size, "asc", "name");
    }

    model.addAttribute("pageContact", pageContact);

    return "user/search_contacts";   // ✅ yahi rahega
}


// Delete contact
@GetMapping("/delete/{id}")
public String deleteContact(
        @PathVariable String id,
        Authentication authentication
) {

    // 🔥 logged-in user
    String email = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(email);

    // 🔥 get contact
    Contact contact = contactService.getById(id);

    // 🔐 SECURITY CHECK
    if (contact == null ||
        !contact.getUser().getUserId().equals(user.getUserId())) {

        return "redirect:/user/contact";
    }

    // 🔥 OPTIONAL: delete image from cloud

    // 🔥 DELETE CONTACT
    contactService.delete(id);

    return "redirect:/user/contact";
}


@GetMapping("/view/{id}")
public String updateContact(
        @PathVariable("id") String id,
        Model model,
        Authentication authentication
) {

    // 🔥 1. Logged-in user
    String email = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(email);

    model.addAttribute("LoggedInUser", user);

    // 🔥 2. Get contact
    Contact contact = contactService.getById(id);

    // ❌ IMPORTANT SECURITY CHECK
    if (contact == null || !contact.getUser().getUserId().equals(user.getUserId())) {
        return "redirect:/user/contact"; // unauthorized access
    }

    // 🔥 3. Convert to form
    ContactForm contactForm = new ContactForm(contact);

    // (optional but safe)
    contactForm.setId(contact.getId());
    contactForm.setPicture(contact.getPictures());

    // 🔥 4. Send to view
    model.addAttribute("contactForm", contactForm);

    return "user/update_contact_view";
}

@PostMapping("/update")
public String updateContact(
        @ModelAttribute ContactForm contactForm,
        Authentication authentication
) {

    String email = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(email);

    // 🔥 existing contact fetch (IMPORTANT)
    Contact contact = contactService.getById(contactForm.getId());

    if (contact == null || 
        !contact.getUser().getUserId().equals(user.getUserId())) {

        return "redirect:/user/contact";
    }

    // 🔥 UPDATE FIELDS (ID ko touch mat karna ❌)
    contact.setName(contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setPhoneNumber(contactForm.getPhoneNumber());
    contact.setAddress(contactForm.getAddress());
    contact.setDescription(contactForm.getDescription());
    contact.setFavorite(contactForm.isFavorite());
    contact.setWebsiteLink(contactForm.getWebsiteLink());
    contact.setLinkedInLink(contactForm.getLinkedInLink());

    // 🔥 IMAGE (sirf ye fix kar)
    if (contactForm.getContactImage() != null 
        && !contactForm.getContactImage().isEmpty()) {

        String imageUrl = imageService.uploadImage(contactForm.getContactImage());

        // ⚠️ IMPORTANT: SAME FIELD use karo (profilePicture ya jo entity me hai)
        contact.setProfilePicture(imageUrl);

    } else {
        contact.setProfilePicture(contactForm.getPicture());
    }

    // 🔥 SAVE (update)
    contactService.save(contact);

    return "redirect:/user/contact";
}


}