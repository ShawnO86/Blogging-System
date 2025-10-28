package com.webdev.bloggingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                .requestMatchers("/api/posts/**").authenticated())
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsServiceForTests(PasswordEncoder passwordEncoder) {
        User.UserBuilder userBuilder = User.builder();
        UserDetails admin = userBuilder.username("TestAdmin")
                .password(passwordEncoder.encode("TestPassword"))
                .roles("USER", "ADMIN").build();

        UserDetails user = userBuilder.username("TestUser")
                .password(passwordEncoder.encode("TestPassword"))
                .roles("USER").build();

        UserDetails user2 = userBuilder.username("TestUser2")
                .password(passwordEncoder.encode("TestPassword"))
                .roles("USER").build();

        return new InMemoryUserDetailsManager(admin, user, user2);
    }

}
