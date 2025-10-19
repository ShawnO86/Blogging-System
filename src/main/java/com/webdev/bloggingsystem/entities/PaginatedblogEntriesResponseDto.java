package com.webdev.bloggingsystem.entities;

import java.util.List;

public record PaginatedblogEntriesResponseDto(
        List<BlogEntryResponseDto> content,
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalEntries,
        boolean isLast,
        boolean isFirst
) {
}
