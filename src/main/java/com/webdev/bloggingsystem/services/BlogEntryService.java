package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;

import java.util.Optional;

public interface BlogEntryService {
    Optional<BlogEntryResponseDto> getBlogEntryById(Integer id);
}
