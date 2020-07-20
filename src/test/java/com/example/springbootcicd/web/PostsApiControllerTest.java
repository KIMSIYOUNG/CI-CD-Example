package com.example.springbootcicd.web;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springbootcicd.application.PostsUpdateRequestDto;
import com.example.springbootcicd.domain.post.Posts;
import com.example.springbootcicd.domain.post.PostsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Test
    void create() {
        String title = "test_title";
        String content = "test_content";
        final PostsSaveRequestDto request = PostsSaveRequestDto.builder()
            .title(title)
            .content(content)
            .author("TEST_AUTHOR")
            .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        final ResponseEntity<Long> response = restTemplate.postForEntity(url, request, Long.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isGreaterThan(0L);

        final List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void update() {
        final Posts savePosts = postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build()
        );

        final Long updateId = savePosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        final PostsUpdateRequestDto request = PostsUpdateRequestDto.builder()
            .title(expectedTitle)
            .content(expectedContent)
            .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        final HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(request);
        final ResponseEntity<Long> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isGreaterThan(0L);

        final List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    void baseEntity() {
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build());
        
        final List<Posts> postsList = postsRepository.findAll();

        assertThat(postsList.get(0).getCreatedAt()).isAfter(now);
        assertThat(postsList.get(0).getUpdatedAt()).isAfter(now);
        System.out.println(postsList.get(0).getCreatedAt());
        System.out.println(postsList.get(0).getUpdatedAt());
    }
}