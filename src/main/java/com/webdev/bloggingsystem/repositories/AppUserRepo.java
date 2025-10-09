package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepo extends CrudRepository<AppUser, Integer> {
}
