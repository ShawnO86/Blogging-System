package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.BlogEntry;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlogEntryRepo extends PagingAndSortingRepository<BlogEntry, Integer> {
}
