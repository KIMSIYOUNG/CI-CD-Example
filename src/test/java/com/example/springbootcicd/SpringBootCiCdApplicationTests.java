package com.example.springbootcicd;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootCiCdApplicationTests {

    @Test
    void contextLoads() {
        assertThat(1==0).isTrue();
    }
}
