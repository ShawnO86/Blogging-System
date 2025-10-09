package com.webdev.bloggingsystem.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {
    private Comment comment;
    private Comment replyComment;
    private BlogEntry mockBlogEntry;
    private AppUser mockUser;

    @BeforeEach
    public void setUp() {
        comment = new Comment();
        replyComment = new Comment();
        comment.setId(1);
        replyComment.setId(2);

        mockBlogEntry = Mockito.mock(BlogEntry.class);
        mockUser = Mockito.mock(AppUser.class);

        Mockito.when(mockUser.getId()).thenReturn(1);
        Mockito.when(mockBlogEntry.getId()).thenReturn(1);
    }

    @Test
    public void testConstructor() {
        Comment testComment = new Comment(
                "Test Comment...",
                mockUser,
                mockBlogEntry
        );

        assertEquals("Test Comment...", testComment.getComment());
        assertEquals(mockUser, testComment.getAuthor());
        assertEquals(mockBlogEntry, testComment.getBlogEntry());
    }

    @Test
    public void testAddRemoveReplyComment() {
        comment.addReply(replyComment);

        assertTrue(comment.getReplies().contains(replyComment));
        assertEquals(replyComment.getParentComment(), comment);

        comment.removeReply(replyComment);
        assertFalse(comment.getReplies().contains(replyComment));
        assertNull(replyComment.getParentComment());

    }
    @Test
    @DisplayName("5. comment entities are equal with same ID")
    public void testEquals() {
        Comment testComment = new Comment();
        testComment.setId(1);

        assertEquals(testComment, comment);
        assertEquals(comment, testComment);
    }

    @Test
    @DisplayName("6. comment entities are not equal with null IDs")
    public void testNotEquals() {
        Comment testComment = new Comment();
        comment.setId(null);

        assertNotEquals(testComment, comment);
        assertNotEquals(comment, testComment);
    }

    @Test
    @DisplayName("7. null id should have hash of 31 and revert to id when persisted")
    public void testNullIdHashCode() {
        Comment testComment = new Comment();
        assertEquals(31, testComment.hashCode());

        testComment.setId(1);
        assertEquals(1, testComment.hashCode());
    }

    @Test
    @DisplayName("8. consistent hashcode to id")
    public void testConsistentHashcode() {
        Comment testComment = new Comment();
        testComment.setId(1);
        assertEquals(1, testComment.hashCode());
        assertEquals(Integer.valueOf(1).hashCode(), testComment.hashCode());
    }
}
