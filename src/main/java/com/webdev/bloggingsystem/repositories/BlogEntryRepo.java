package com.webdev.bloggingsystem.repositories;

import com.webdev.bloggingsystem.entities.BlogEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogEntryRepo extends CrudRepository<BlogEntry, Integer>, PagingAndSortingRepository<BlogEntry, Integer> {
    @EntityGraph(value = "eager-fetch-categories-author", type = EntityGraph.EntityGraphType.LOAD)
    Optional<BlogEntry> findBlogEntryByIdAndAuthorUsername(Integer id, String authorUsername);

    @EntityGraph(value = "eager-fetch-all-collections-author", type = EntityGraph.EntityGraphType.LOAD)
    Optional<BlogEntry> findBlogEntryById(Integer id);

    @EntityGraph(value = "eager-fetch-categories-author", type = EntityGraph.EntityGraphType.LOAD)
    Page<BlogEntry> findAllByIsPublicTrue(Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM Blog_Entries WHERE id = :id", nativeQuery = true)
    void betterDeleteById(@Param("id") Integer id);
}
