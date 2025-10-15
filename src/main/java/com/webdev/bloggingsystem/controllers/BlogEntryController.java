package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;
import com.webdev.bloggingsystem.services.BlogEntryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
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


    @GetMapping("/posts")
    public ResponseEntity<List<BlogEntryResponseDto>> getBlogEntries(Pageable pageable) {
        return ResponseEntity.ok(blogEntryService.getBlogEntries(pageable));
    }


    // ToDo: need to add principle to get "author" & validate BlogEntryRequestDto fields...
    @PostMapping("/posts")
    public ResponseEntity<Void> createBlogEntry(@RequestBody BlogEntryRequestDto blogEntryRequestDto,
                                                /*Principal principal,*/ UriComponentsBuilder ucb) {

        BlogEntry blogEntryToSave = blogEntryService.saveEntry(blogEntryRequestDto, "TestUser");

        URI location = ucb.path("/api/posts/{id}").buildAndExpand(blogEntryToSave.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
