package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.AppUser;
import com.webdev.bloggingsystem.entities.RegistrationDto;
import com.webdev.bloggingsystem.entities.Role;
import com.webdev.bloggingsystem.entities.RoleType;
import com.webdev.bloggingsystem.exceptions.UsernameInUseException;
import com.webdev.bloggingsystem.repositories.AppUserRepo;
import com.webdev.bloggingsystem.repositories.RoleRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           AppUserRepo appUserRepo,
                           RoleRepo roleRepo,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.appUserRepo = appUserRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegistrationDto registrationDto) {
        if (appUserRepo.existsByUsername(registrationDto.username())) {
            throw new UsernameInUseException(registrationDto.username() +  " is already in use!");
        }

        AppUser appUser = this.mapDtoToAppUser(registrationDto);
        appUserRepo.save(appUser);
    }

    private AppUser mapDtoToAppUser(RegistrationDto registrationDto) {
        AppUser appUser = new AppUser(
                registrationDto.username(),
                passwordEncoder.encode(registrationDto.password()),
                registrationDto.email()
        );
        Role role = roleRepo.findByRole(RoleType.USER);
        appUser.setRoles(Set.of(role));

        return appUser;
    }
}
