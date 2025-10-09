package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Integer> {
}
