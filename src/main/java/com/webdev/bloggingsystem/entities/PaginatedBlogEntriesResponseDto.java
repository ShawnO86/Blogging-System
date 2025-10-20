package com.webdev.bloggingsystem.entities;

import java.util.List;

public record PaginatedBlogEntriesResponseDto(
        List<BlogEntryResponseDto> entries,
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalEntries,
        boolean isLast,
        boolean isFirst
) {
}
