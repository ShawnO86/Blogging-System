package com.webdev.bloggingsystem.entities;

import java.util.List;

public record BlogEntryRequestDto(
        String title,
        String content,
        List<String> categories,
        boolean isPublic
) {}