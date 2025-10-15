package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.*;
import com.webdev.bloggingsystem.repositories.AppUserRepo;
import com.webdev.bloggingsystem.repositories.BlogEntryRepo;

import com.webdev.bloggingsystem.repositories.CategoryRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class BlogEntryServiceImpl implements BlogEntryService {
    private final BlogEntryRepo blogEntryRepo;
    private final AppUserRepo appUserRepo;
    private final CategoryRepo categoryRepo;

    public BlogEntryServiceImpl(BlogEntryRepo blogEntryRepo, AppUserRepo appUserRepo, CategoryRepo categoryRepo) {
        this.blogEntryRepo = blogEntryRepo;
        this.appUserRepo = appUserRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Optional<BlogEntryResponseDto> getBlogEntryById(Integer id) {
        Optional<BlogEntry> entry = blogEntryRepo.findBlogEntryByIdEagerLoadAll(id);

        if (entry.isPresent()) {
            return Optional.of(new BlogEntryResponseDto(entry.get(), true));
        }
        return Optional.empty();
    }

    @Override
    public List<BlogEntryResponseDto> getBlogEntries(Pageable pageable) {
        // default descending sort by updatedAt
        Page<BlogEntry> blogEntries = blogEntryRepo.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "updatedAt"))
                )
        );
        List<BlogEntryResponseDto> responseDtos = new ArrayList<>();
        for (BlogEntry blogEntry : blogEntries.getContent()) {
            responseDtos.add(new BlogEntryResponseDto(blogEntry, false));
        }
        return responseDtos;
    }

    // todo: change to using principle when security enabled & validate entry.
    @Override
    public BlogEntry saveEntry(BlogEntryRequestDto blogEntryRequestDto, String principleName) {
        AppUser author = appUserRepo.findByUsername(principleName);
        Set<Category> categories = categoryRepo.findByCategoryNameIn(blogEntryRequestDto.categories());

        return blogEntryRepo.save(this.mapRequestToEntity(blogEntryRequestDto, author, categories));
    }


    private BlogEntry mapRequestToEntity(BlogEntryRequestDto blogEntryRequestDto, AppUser author,
                                         Set<Category> categories) {
        return new BlogEntry(
                author,
                blogEntryRequestDto.title(),
                blogEntryRequestDto.content(),
                blogEntryRequestDto.isPublic(),
                categories
        );
    }
}
