package com.webdev.bloggingsystem.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Blog_Entries")
// NamedEntityGraph needed for JPA eager loading without N+1
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "eager-fetch-categories-author",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("categories")
        }
    ),
    @NamedEntityGraph(
        name = "eager-fetch-all-collections-author",
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("categories"),
                @NamedAttributeNode("comments")
        }
    )
})

public class BlogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "date_published", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "date_updated",  nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private AppUser author;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "Posts_Categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories =  new HashSet<>();

    @OneToMany(mappedBy = "blogEntry", orphanRemoval = true, fetch = FetchType.LAZY)
    @SQLRestriction("parent_comment_id IS NULL")
    private Set<Comment> comments = new HashSet<>();

    public BlogEntry() {}

    public BlogEntry(AppUser author, String title, String content, boolean isPublic, Set<Category> categories) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public AppUser getAuthor() {
        return author;
    }
    public void setAuthor(AppUser author) {
        this.author = author;
    }
    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    public Set<Comment> getComments() {
        return comments;
    }
    public void addCategory(Category category) {
        this.categories.add(category);
    }
    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBlogEntry(this);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setBlogEntry(null);
    }

    @Override
    public String toString() {
        return "BlogEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isPublic=" + isPublic +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // compares only id fields, returns false for entities not persisted - where this.id == null
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass() || this.getId() == null) return false;
        BlogEntry entry = (BlogEntry) o;
        return Objects.equals(this.getId(), entry.getId());
    }

    // sets hashcode for entities not persisted to 31 as temporary fallback
    @Override
    public int hashCode() {
        if (this.getId() == null) return 31;
        return this.getId().hashCode();
    }
}
