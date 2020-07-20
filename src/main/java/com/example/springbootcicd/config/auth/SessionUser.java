package com.example.springbootcicd.config.auth;

import java.io.Serializable;

import com.example.springbootcicd.domain.user.User;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(final User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
