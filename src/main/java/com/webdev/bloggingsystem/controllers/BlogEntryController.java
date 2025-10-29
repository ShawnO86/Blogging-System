package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;
import com.webdev.bloggingsystem.entities.PaginatedBlogEntriesResponseDto;
import com.webdev.bloggingsystem.services.BlogEntryService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class BlogEntryController {
    private final BlogEntryService blogEntryService;

    public BlogEntryController(BlogEntryService blogEntryService) {
        this.blogEntryService = blogEntryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogEntryResponseDto> getBlogEntry(@PathVariable Integer id, Principal principal) {
        String username;
        if (principal != null) {
            username = principal.getName();
        } else {
            username = null;
        }
        return ResponseEntity.ok(blogEntryService.getBlogEntryById(id, username));
    }

    @GetMapping()
    public ResponseEntity<PaginatedBlogEntriesResponseDto> getAllPublicBlogEntries(Pageable pageable) {
        return ResponseEntity.ok(blogEntryService.getAllPublicBlogEntries(pageable));
    }

    // ToDo: need to validate BlogEntryRequestDto fields (in service layer)...
    @PostMapping()
    public ResponseEntity<Void> createBlogEntry(@RequestBody BlogEntryRequestDto blogEntryRequestDto,
                                                Principal principal, UriComponentsBuilder ucb) {
        return ResponseEntity.created(blogEntryService.saveEntry(blogEntryRequestDto, principal.getName(), ucb)).build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<Void> updateBlogEntry(@PathVariable Integer id,
                                                 @RequestBody BlogEntryRequestDto blogEntryRequestDto,
                                                 Principal principal) {
        blogEntryService.updateEntryById(id, blogEntryRequestDto, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogEntry(@PathVariable Integer id, Principal principal) {
        blogEntryService.deleteEntryById(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
