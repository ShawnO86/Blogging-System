package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BlogEntryService {
    Optional<BlogEntryResponseDto> getBlogEntryById(Integer id);
    List<BlogEntryResponseDto> getBlogEntries(Pageable pageable);
    BlogEntry saveEntry(BlogEntryRequestDto blogEntryRequestDto, String principalName);

}
