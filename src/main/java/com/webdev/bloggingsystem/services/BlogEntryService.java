package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntry;

import java.util.Optional;

public interface BlogEntryService {
    Optional<BlogEntry> getBlogEntryById(Integer id);
}
