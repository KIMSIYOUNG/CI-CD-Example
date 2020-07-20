package com.example.springbootcicd.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.springbootcicd.application.PostsUpdateRequestDto;
import com.example.springbootcicd.domain.post.Posts;
import com.example.springbootcicd.domain.post.PostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
        postsRepository.deleteAll();
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Test
    @WithMockUser(roles="USER")
    void create() throws Exception {
        String title = "test_title";
        String content = "test_content";
        final PostsSaveRequestDto request = PostsSaveRequestDto.builder()
            .title(title)
            .content(content)
            .author("TEST_AUTHOR")
            .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    void update() throws Exception {
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
        mockMvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo("title2");
        assertThat(all.get(0).getContent()).isEqualTo("content2");
    }

    @Test
    void baseEntity() {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
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