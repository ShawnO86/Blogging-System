package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.AppUser;
import com.webdev.bloggingsystem.entities.LoginDto;
import com.webdev.bloggingsystem.entities.RegistrationDto;
import com.webdev.bloggingsystem.repositories.AppUserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import org.junit.jupiter.api.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // <-- for H2 testing, change to runtime for MySQL
public class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AppUserRepo appUserRepo;

    @Test
    @DisplayName("1. admin should be allowed to register user")
    public void registerUserAsAdmin() {
        RegistrationDto registrationDto = new RegistrationDto(
                "RegisterTest", "TestPassword", "TestEmail@email.com");

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestAdmin", "TestPassword")
                .postForEntity("/api/auth/register", registrationDto, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("User Registration Successful", response.getBody());

        Optional<AppUser> appUser = appUserRepo.findByUsername("RegisterTest");
        Assertions.assertTrue(appUser.isPresent());
        System.out.println("New Registered User: " + appUser.get());
    }

    @Test
    @DisplayName("2. user should not be allowed to register user")
    public void registerUserAsUser() {
        RegistrationDto registrationDto = new RegistrationDto(
                "RegisterTest", "TestPassword", "TestEmail@email.com");

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .postForEntity("/api/auth/register", registrationDto, String.class);

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        Optional<AppUser> appUser = appUserRepo.findByUsername("RegisterTest");
        Assertions.assertFalse(appUser.isPresent());
    }

    @Test
    @DisplayName("3. should not allow user to be registered with already used username")
    public void registerUserUsedUsername() {
        RegistrationDto registrationDto = new RegistrationDto(
                "TestUser", "TestPassword", "TestEmail@email.com");

        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestAdmin", "TestPassword")
                .postForEntity("/api/auth/register", registrationDto, String.class);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        System.out.println(response);
    }

    @Test
    @DisplayName("4. should login existing user")
    public void loginUser() {
        LoginDto loginDto = new LoginDto("TestUser", "TestPassword");

        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/auth/login", loginDto, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Login Successful", response.getBody());
    }

    @Test
    @DisplayName("5. should login existing user")
    public void loginUserBadCredentials() {
        LoginDto loginDto = new LoginDto("TestUser", "BadPassword");

        ResponseEntity<String> response = restTemplate
                .postForEntity("/api/auth/login", loginDto, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
