package com.webdev.bloggingsystem.services;


import com.webdev.bloggingsystem.entities.*;
import com.webdev.bloggingsystem.exceptions.UsernameInUseException;
import com.webdev.bloggingsystem.repositories.AppUserRepo;
import com.webdev.bloggingsystem.repositories.RoleRepo;

import com.webdev.bloggingsystem.security.JwtTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final static Logger logger  = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthServiceImpl(AppUserRepo appUserRepo,
                           RoleRepo roleRepo,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenGenerator jwtTokenGenerator) {
        this.appUserRepo = appUserRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    public void registerUser(RegistrationDto registrationDto) {
        if (appUserRepo.existsByUsername(registrationDto.username())) {
            throw new UsernameInUseException(registrationDto.username() +  " is already in use!");
        }

        AppUser appUser = this.mapDtoToAppUser(registrationDto);
        appUserRepo.save(appUser);
    }

    @Override
    public AuthResponseDto loginUser(LoginDto loginDto) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.username(),
                            loginDto.password()
                    )
            );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        String jwt = jwtTokenGenerator.generateToken(authentication);
        return new AuthResponseDto(jwt);
    }


    private AppUser mapDtoToAppUser(RegistrationDto registrationDto) {
        AppUser appUser = new AppUser(
                registrationDto.username(),
                passwordEncoder.encode(registrationDto.password()),
                registrationDto.email()
        );
        Role role = roleRepo.findByRole(RoleType.ROLE_USER);
        appUser.setRoles(Set.of(role));

        return appUser;
    }
}
