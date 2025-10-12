package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.BlogEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlogEntryRepo extends CrudRepository<BlogEntry, Integer>, PagingAndSortingRepository<BlogEntry, Integer> {

}
