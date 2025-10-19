package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;

import com.webdev.bloggingsystem.entities.PaginatedblogEntriesResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogEntryService {
    Optional<BlogEntryResponseDto> getBlogEntryById(Integer id, String principalName);
    PaginatedblogEntriesResponseDto getAllPublicBlogEntries(Pageable pageable);
    BlogEntry saveEntry(BlogEntryRequestDto blogEntryRequestDto, String principalName);

}
