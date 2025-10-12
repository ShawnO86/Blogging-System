package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.repositories.BlogEntryRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogEntryServiceImpl implements BlogEntryService {
    BlogEntryRepo blogEntryRepo;

    public BlogEntryServiceImpl(BlogEntryRepo blogEntryRepo) {
        this.blogEntryRepo = blogEntryRepo;
    }

    @Override
    public Optional<BlogEntry> getBlogEntryById(Integer id) {

        return blogEntryRepo.findById(id);
    }
}
