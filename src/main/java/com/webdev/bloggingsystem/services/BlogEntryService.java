package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;

import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface BlogEntryService {
    Optional<BlogEntryResponseDto> getBlogEntryById(Integer id, String principalName);
    List<BlogEntryResponseDto> getAllPublicBlogEntries(Pageable pageable);
    BlogEntry saveEntry(BlogEntryRequestDto blogEntryRequestDto, String principalName);

}
