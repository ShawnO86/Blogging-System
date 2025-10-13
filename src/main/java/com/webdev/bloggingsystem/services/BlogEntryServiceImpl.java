package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;
import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.repositories.BlogEntryRepo;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BlogEntryServiceImpl implements BlogEntryService {
    private final BlogEntryRepo blogEntryRepo;

    public BlogEntryServiceImpl(BlogEntryRepo blogEntryRepo) {
        this.blogEntryRepo = blogEntryRepo;
    }

    @Override
    public Optional<BlogEntryResponseDto> getBlogEntryById(Integer id) {
        Optional<BlogEntry> entry = blogEntryRepo.findById(id);

        if (entry.isPresent()) {
            return Optional.of(new BlogEntryResponseDto(entry.get()));
        }
        return Optional.empty();
    }
}
