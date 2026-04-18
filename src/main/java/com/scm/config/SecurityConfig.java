package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

//     @Bean
//     public UserDetailsService userDetailsService() {

//         UserDetails user1 = User.withDefaultPasswordEncoder()
//                 .username("user1")
//                 .password("user1")
//                 .roles("USER")
//                 .build();

//         var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);

//         return inMemoryUserDetailsManager;
//     }

@Autowired
private SecurityCustomUserDetailService userDetailsService;

@Autowired
private OAuthAuthenticationSuccessfulHandler oAuthAuthenticationSuccessfulHandler;

@Bean
public AuthenticationProvider authProvider() {
    
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;

}
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.authorizeHttpRequests(authorize -> {authorize
            .requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        //Saved Request Mechanism
        httpSecurity.formLogin(formLogin -> {

        formLogin.loginPage("/login");
        formLogin.loginProcessingUrl("/authenticate");
        formLogin.successForwardUrl("/user/profile");
        formLogin.failureUrl("/login?error=true");
        formLogin.usernameParameter("email");
        formLogin.passwordParameter("password");
        
    });

    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.logout(logout -> {
        logout.logoutUrl("/do-logout");
        logout.logoutSuccessUrl("/login?logout=true");
    });

    httpSecurity.oauth2Login(oauth2Login -> {
        oauth2Login.loginPage("/login");
        oauth2Login.successHandler(oAuthAuthenticationSuccessfulHandler);
    });

    return httpSecurity.build();
}

@Bean
public PasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
    }
}
