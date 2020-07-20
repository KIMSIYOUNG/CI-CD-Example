package com.example.springbootcicd.domain.post;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    void clean() {
        postsRepository.deleteAll();
    }

    @Test
    void createAndSave() {
        String title = "test_title";
        String content = "test_content";

        postsRepository.save(Posts.builder()
            .title(title)
            .content(content)
            .author("kyle@gmail.com")
            .build());

        final List<Posts> posts = postsRepository.findAll();

        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }
}