package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.AuthResponseDto;
import com.webdev.bloggingsystem.entities.LoginDto;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        AuthResponseDto authResponseDto = authService.loginUser(loginDto);
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDto registrationDto) {
        authService.registerUser(registrationDto);
        return ResponseEntity.ok("User Registration Successful");
    }
}