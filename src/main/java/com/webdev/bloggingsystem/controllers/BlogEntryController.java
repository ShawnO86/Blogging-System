package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;
import com.webdev.bloggingsystem.services.BlogEntryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BlogEntryController {
    private final static Logger logger  = LoggerFactory.getLogger(BlogEntryController.class);

    private final BlogEntryService blogEntryService;

    public BlogEntryController(BlogEntryService blogEntryService) {
        this.blogEntryService = blogEntryService;
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<BlogEntryResponseDto> getBlogEntry(@PathVariable Integer id) {
        Optional<BlogEntryResponseDto> blogEntry = blogEntryService.getBlogEntryById(id);

        logger.debug("blogEntry optional: {}", blogEntry);

        if (blogEntry.isPresent()) {
            return ResponseEntity.ok(blogEntry.get());
        }
        return ResponseEntity.notFound().build();
    }
}
