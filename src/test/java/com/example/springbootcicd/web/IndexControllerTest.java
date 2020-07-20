package com.example.springbootcicd.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void mainPageLoading() {
        String body = this.restTemplate.getForObject("/", String.class);

        assertThat(body).contains("스프링부트로 시작하는 웹 서비스 Ver.2");
    }
}