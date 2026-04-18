package com.scm.helpers;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthenticationToken) {

            // goggle login
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            ;
            String authorizedClientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            if (authorizedClientRegistrationId.equals("google")) {
                // google login
                DefaultOAuth2User user = (DefaultOAuth2User) oauthToken.getPrincipal();

                String email = user.getAttribute("email");
                return email;
            } else if (authorizedClientRegistrationId.equals("github")) {
                DefaultOAuth2User user = (DefaultOAuth2User) oauthToken.getPrincipal();

                String email = user.getAttribute("email");

                // ⚠️ GitHub email nahi deta → fallback banana padega
                if (email == null) {
                    email = user.getName() + "@github.local";
                }

                return email;
            } else {

                // other login
                System.out.println("Other login method: " + authorizedClientRegistrationId);
                return authentication.getName();
            }

        }

        return authentication.getName();

    }
}
