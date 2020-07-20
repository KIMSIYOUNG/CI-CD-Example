package com.example.springbootcicd.config.auth;

import java.util.Map;

import com.example.springbootcicd.domain.user.Role;
import com.example.springbootcicd.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(final Map<String, Object> attributes, final String nameAttributeKey, final String name,
        final String email,
        final String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(final String id, final Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
            .nameAttributeKey((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("profileImage"))
            .attributes(response)
            .nameAttributeKey(id)
            .build();
    }

    private static OAuthAttributes ofGoogle(final String userNameAttributeName, final Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .name(name)
            .email(email)
            .picture(picture)
            .role(Role.GUEST)
            .build();
    }
}
