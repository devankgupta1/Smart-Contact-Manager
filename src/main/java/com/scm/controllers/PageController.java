package com.scm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.scm.forms.UserForm;
import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(Model model) {

        // sending data to view
        System.out.println("Home page Handler");
        model.addAttribute("name", "Welcome to SCM Application");
        model.addAttribute("profile", "https://devankgupta.netlify.app");
        model.addAttribute("github", "https://github.com/devankgupta1");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        System.out.println("About page Handler");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage(Model model) {

        model.addAttribute("isLogin", true);
        System.out.println("About page Handler");

        return "services";
    }

    @RequestMapping("/contact")
    public String contactPage(Model model) {
        System.out.println("Contact page Handler");
        return "contact";
    }

    @RequestMapping("/login")
    public String loginPage(Model model) {
        System.out.println("Login page Handler");
        return "login";
    }


    @RequestMapping("/signup")
    public String signupPage(Model model, HttpSession session) {

        model.addAttribute("userForm", new UserForm());

        Object msg = session.getAttribute("registerMessage");
        if (msg != null) {
            model.addAttribute("registerMessage", msg);
            session.removeAttribute("registerMessage");
        }

        return "signup";
    }

    @PostMapping("/do-register")
    public String processRegister(@ModelAttribute UserForm userForm,
                                  HttpSession session) {

        User user = new User();
        user.setName(userForm.getName());
        user.setEmailId(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());

        userService.saveUser(user);

        Message message = Message.builder()
                .content("Registration successful! Please login.")
                .build();

        session.setAttribute("registerMessage", message);

        return "redirect:/signup";
    }

}
