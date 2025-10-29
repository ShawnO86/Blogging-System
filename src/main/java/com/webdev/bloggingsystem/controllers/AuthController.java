package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.RegistrationDto;
import com.webdev.bloggingsystem.services.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
        authService.registerUser(registrationDto);
        return ResponseEntity.ok("User Registration Successful");
    }

}
