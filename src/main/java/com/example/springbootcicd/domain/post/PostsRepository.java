package com.example.springbootcicd.domain.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p order by p.id DESC ")
    List<Posts> findALlDesc();
}
