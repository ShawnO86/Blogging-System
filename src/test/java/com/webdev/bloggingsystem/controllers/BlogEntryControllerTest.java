package com.webdev.bloggingsystem.controllers;

import com.webdev.bloggingsystem.entities.BlogEntryRequestDto;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // <-- for H2 testing, comment out for MySQL
public class BlogEntryControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("1. found id")
    void getBlogEntryById() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts/1", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

        System.out.println("response: " + response);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        String content = documentContext.read("$.content");

        System.out.println("json: " + documentContext.jsonString());

        assertNotNull(id);
        assertEquals(1, id);
        assertEquals("Test Post 1 - TestAdmin content is here.", content);
    }

    @Test
    @DisplayName("2. not found id")
    void notFoundBlogEntryById() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts/99", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 NOT FOUND");
        System.out.println("response: " + response);

        assertNull(response.getBody());
    }

    @Test
    @DisplayName("3. create and persist new BlogEntry")
    @DirtiesContext // <-- needed to restart application after adding this new data so tests stay consistent with data.sql
    void createBlogEntry() {
        BlogEntryRequestDto blogEntryRequestDto = new BlogEntryRequestDto(
                "Testing Http POST",
                "This entry is for testing the Http POST method.",
                        List.of("Test Category 1", "Test Category 2"),
                        true
        );
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .postForEntity("/api/posts", blogEntryRequestDto, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Should return 201 Created");

        URI uri = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity(uri, String.class);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        System.out.println("json: " + documentContext.jsonString());
        Number id = documentContext.read("$.id");
        String content = documentContext.read("$.content");
        JSONArray categories = documentContext.read("$.categories");

        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Should return 200 OK");
        assertEquals(4, id);
        assertEquals("This entry is for testing the Http POST method.", content);
        assertEquals("Test Category 1", categories.getFirst());
        assertEquals("Test Category 2", categories.get(1));
    }

    @Test
    @DisplayName("4. should return all public BlogEntries")
    void getAllPublicBlogEntries() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts?sort=createdAt,asc", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");
        System.out.println("response: " + response.getBody());

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        // double . to return list of all values of specified key ..
        JSONArray ids = documentContext.read("$..id");
        JSONArray titles = documentContext.read("$..title");

        // Entry with id 2 is private and should not be included
        assertEquals(2, ids.size());
        assertEquals(List.of(1, 3), ids);

        assertEquals(2, titles.size());
        assertEquals(List.of("Test Post 1", "Test Post 3"), titles);
    }

    @Test
    @DisplayName("5. should return page of BlogEntry")
    void getBlogEntryAsPage() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts?page=0&size=1", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");
        System.out.println("response: " + response.getBody());
    }

    @Test
    @DisplayName("6. should return sorted page of BlogEntries (last entry by date id=3")
    void getBlogEntryAsSortedPage() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts?page=0&size=1&sort=createdAt,desc", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        System.out.println("json: " + documentContext.jsonString());
        String title  = documentContext.read("$[0].title");

        assertEquals("Test Post 3", title);
    }

    @Test
    @DisplayName("7. should return sorted page using default pageable (descending sort by updatedAt)")
    void getBlogEntryAsSortedPageUsingDefaultPageable() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK");

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray ids = documentContext.read("$..id");
        JSONArray titles = documentContext.read("$..title");

        // Entry with id 2 is private and should not be included
        assertEquals(2, ids.size());
        assertEquals(List.of(3, 1), ids);

        assertEquals(2, titles.size());
        assertEquals(List.of("Test Post 3", "Test Post 1"), titles);
    }

    @Test
    @DisplayName("8. should not return entry using bad credentials")
    void blogEntryWithBadCredentials() {
        // wrong user, existing password
        ResponseEntity<String> response1 = restTemplate
                .withBasicAuth("NotAUser", "TestPassword")
                .getForEntity("/api/posts/1", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode(), "Should return 401 UNAUTHORIZED");

        // right user, wrong password
        ResponseEntity<String> response2 = restTemplate
                .withBasicAuth("TestUser", "BadPassword")
                .getForEntity("/api/posts/1", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response2.getStatusCode(), "Should return 401 UNAUTHORIZED");
    }

    @Test
    @DisplayName("9. should not allow private entry to be viewed by non-author")
    void getBlogEntryWithNonAuthor() {
        // test data = BlogEntry with id 2 is private and owned by TestAdmin.
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("TestUser", "TestPassword")
                .getForEntity("/api/posts/2", String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Should return 403 FORBIDDEN");
        System.out.println("response: " + response);
    }

}
