package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.AppUser;
import com.webdev.bloggingsystem.entities.RegistrationDto;
import com.webdev.bloggingsystem.repositories.AppUserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // <-- for H2 testing, comment out for MySQL
public class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AppUserRepo appUserRepo;

    @Test
    @DisplayName("1. register user")
    public void registerUser() {
        RegistrationDto registrationDto = new RegistrationDto(
                "RegisterTest", "TestPassword", "TestEmail@email.com");

        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/auth/register", registrationDto, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Registration Successful", response.getBody());

        Optional<AppUser> appUser = appUserRepo.findByUsername("RegisterTest");
        assertTrue(appUser.isPresent());
        System.out.println(appUser.get());
    }
}
