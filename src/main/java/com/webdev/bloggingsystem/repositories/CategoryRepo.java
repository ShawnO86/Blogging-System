package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface CategoryRepo extends CrudRepository<Category, Integer> {
    Set<Category> findByCategoryNameIn(List<String> categories);
}
