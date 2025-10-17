package com.webdev.bloggingsystem.services;

import com.webdev.bloggingsystem.entities.AppUser;
import com.webdev.bloggingsystem.entities.BlogEntry;
import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;
import com.webdev.bloggingsystem.entities.BlogEntryResponseDto;
import com.webdev.bloggingsystem.entities.Category;
import com.webdev.bloggingsystem.repositories.AppUserRepo;
import com.webdev.bloggingsystem.repositories.BlogEntryRepo;
import com.webdev.bloggingsystem.repositories.CategoryRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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
    public Optional<BlogEntryResponseDto> getBlogEntryById(Integer id, String principalName) {
        Optional<BlogEntry> entry = blogEntryRepo.findBlogEntryByIdEagerLoadAll(id);

        if (entry.isPresent()) {
            if (!entry.get().isPublic() && !entry.get().getAuthor().getUsername().equals(principalName)) {
                throw new AccessDeniedException("Access denied");
            } else {
                return Optional.of(new BlogEntryResponseDto(entry.get(), true));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<BlogEntryResponseDto> getAllPublicBlogEntries(Pageable pageable) {
        // default is descending sort by updatedAt, pageSize 20, pageNumber 0
        Page<BlogEntry> blogEntries = blogEntryRepo.findAllByIsPublicTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, "updatedAt"))
                )
        );
        System.out.println("Pageable: " + blogEntries.getPageable());

        List<BlogEntryResponseDto> responseDtos = new ArrayList<>();
        for (BlogEntry blogEntry : blogEntries.getContent()) {
            responseDtos.add(new BlogEntryResponseDto(blogEntry, false));
        }
        return responseDtos;
    }

    // todo: validate entry before saving.
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
