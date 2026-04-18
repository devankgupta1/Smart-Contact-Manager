package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repository.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessfulHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessfulHandler.class);

    @Autowired
    UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuth Authentication successful for user: " + authentication.getName());

        // identify the user
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info("Authorized Client Registration ID: " + authorizedClientRegistrationId);

        var oauthuser = (DefaultOAuth2User) authentication.getPrincipal();
        oauthuser.getAttributes().forEach((key, value) -> {
            logger.info("Attribute: " + key + " = " + value);
        });

        User user = new User();
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);

    if (authorizedClientRegistrationId.equals("google")) {
        logger.info("User logged in with Google");

        user.setEmailId(oauthuser.getAttributes().get("email").toString());
        user.setPicture(oauthuser.getAttributes().get("picture").toString());
        user.setName(oauthuser.getAttributes().get("name").toString());
        user.setProviderUserId(oauthuser.getName());
        user.setProvider(Providers.GOOGLE);
    } else if (authorizedClientRegistrationId.equals("github")) {

    String email = (String) oauthuser.getAttributes().get("email");

    if (email == null) {
        email = oauthuser.getName() + "@github.local";
    }

    user.setEmailId(email);
    user.setName((String) oauthuser.getAttributes().get("login"));
    user.setPicture((String) oauthuser.getAttributes().get("avatar_url"));
    user.setProviderUserId(oauthuser.getName());
    user.setProvider(Providers.GITHUB);
}

// check if user already exists
        User existingUser = userRepo.findByEmailId(user.getEmailId()).orElse(null);

        if (existingUser == null) {
           userRepo.saveAndFlush(user);
            logger.info("User saved successfully: " + user.getEmailId());
        } else {
            logger.info("User already exists: " + user.getEmailId());
        }

        response.sendRedirect("/user/profile");


    //     DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

    //     String email = user.getAttribute("email");
    //     String name = user.getAttribute("name");
    //     String picture = user.getAttribute("picture");

    //     User user1 = new User();
    //     user1.setEmailId(email);
    //     user1.setName(name);
    //     user1.setPicture(picture);
    //     user1.setPassword("password");
    //     user1.setProviderUserId(UUID.randomUUID().toString());
    //     user1.setProvider(Providers.GOOGLE);
    //     user1.setEnabled(true);

    //     user1.setEmailVerified(true);
    //     user1.setProviderUserId(user.getName());
    //     user1.setRoleList(List.of(AppConstants.ROLE_USER));
    //     user1.setAbout("This is " + name + " profile maid with google/oauth2 login.");

    //    User user2 = userRepo.findByEmailId(email).orElse(null);

    //    if(user2 == null) {
    //     userRepo.save(user1);
    //     logger.info("user saved successfully with email: " + email);
    //    }
    //     else {
    //     logger.info("user already exists with email: " + email);
    //    }

    //     logger.info(user.getName() + " logged in with email: " + email);

    //     user.getAttributes().forEach((key, value) -> {
    //         logger.info("Attribute: " + key + " = " + value);
    //     });

    //     logger.info(user.getAuthorities().toString());

    //     response.sendRedirect("/user/profile");
    }
}