package com.webdev.bloggingsystem.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record BlogEntryResponseDto(
        Integer id,
        String author,
        String title,
        String content,
        Instant createdAt,
        Instant updatedAt,
        List<String> categories,
        List<CommentResponseDto> comments
) {
    public BlogEntryResponseDto(BlogEntry entry) {
        Set<Category> categorySet = entry.getCategories();
        Set<Comment> commentSet = entry.getComments();
        List<String> categories = new ArrayList<>(categorySet.size());
        List<CommentResponseDto> comments = new ArrayList<>(commentSet.size());
        CommentResponseDto commentResponse;

        for (Category curr : categorySet) {
            categories.add(curr.getCategoryName());
        }

        for (Comment curr : commentSet) {
            commentResponse = new CommentResponseDto(
                    curr.getId(),
                    curr.getComment(),
                    curr.getCreatedAt(),
                    curr.getAuthor().getUsername()
            );
            comments.add(commentResponse);
        }

        // "this" default constructor
        this(
            entry.getId(),
            entry.getAuthor().getUsername(),
            entry.getTitle(),
            entry.getContent(),
            entry.getCreatedAt(),
            entry.getUpdatedAt(),
            categories,
            comments
        );
    }
}
