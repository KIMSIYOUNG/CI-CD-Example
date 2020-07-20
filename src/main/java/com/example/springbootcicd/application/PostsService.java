package com.example.springbootcicd.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootcicd.domain.post.Posts;
import com.example.springbootcicd.domain.post.PostsRepository;
import com.example.springbootcicd.web.PostsResponseDto;
import com.example.springbootcicd.web.PostsSaveRequestDto;
import lombok.AllArgsConstructor;

@Transactional
@AllArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    public Long save(final PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public Long update(final Long id, final PostsUpdateRequestDto requestDto) {
        final Posts posts = find(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(final Long id) {
        final Posts posts = PostsService.this.find(id);
        return new PostsResponseDto(posts);
    }

    private Posts find(final Long id) {
        return postsRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
