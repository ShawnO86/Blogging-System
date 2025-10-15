package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.BlogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface BlogEntryRepo extends CrudRepository<BlogEntry, Integer>, PagingAndSortingRepository<BlogEntry, Integer> {
    @Query("SELECT b FROM BlogEntry b " +
        "JOIN FETCH b.author " +
        "LEFT JOIN FETCH b.categories " +
        "LEFT JOIN FETCH b.comments " +
        "WHERE b.id = :id")
    Optional<BlogEntry> findBlogEntryByIdEagerLoadAll(@Param("id") Integer id);

    @EntityGraph(value = "fetch-with-pageable", type = EntityGraph.EntityGraphType.LOAD)
    @Override
    @NonNull
    Page<BlogEntry> findAll(@NonNull Pageable pageable);
}
