package com.example.springbootcicd.application;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostsUpdateRequestDto {
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
