package com.webdev.bloggingsystem.config;

import com.webdev.bloggingsystem.entities.AppUser;
import com.webdev.bloggingsystem.entities.Role;
import com.webdev.bloggingsystem.repositories.AppUserRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final static Logger logger  = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final AppUserRepo userRepo;

    public CustomUserDetailsService(AppUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.debug("Loading user by username: {}", username);
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        logger.debug("User {} loaded", user);

        return new User(user.getUsername(), user.getPassword(), this.mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        logger.debug("Loading user roles: {}", roles.toString());
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        }
        return authorities;
    }
}
