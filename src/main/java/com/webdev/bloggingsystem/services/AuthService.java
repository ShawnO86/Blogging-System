package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.AuthResponseDto;
import com.webdev.bloggingsystem.entities.LoginDto;
import com.webdev.bloggingsystem.entities.RegistrationDto;

public interface AuthService {
    void registerUser(RegistrationDto registrationDto);
    AuthResponseDto loginUser(LoginDto loginDto);
}
